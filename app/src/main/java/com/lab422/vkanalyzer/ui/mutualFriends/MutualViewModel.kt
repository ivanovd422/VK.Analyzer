package com.lab422.vkanalyzer.ui.mutualFriends

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplitude.api.Amplitude
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider.FriendsListDataProvider
import com.lab422.vkanalyzer.ui.mutualFriends.model.MutualFriendsModel
import com.lab422.vkanalyzer.utils.extensions.debounce
import com.lab422.vkanalyzer.utils.navigator.Navigator
import com.lab422.vkanalyzer.utils.requests.GetUserIdCommand
import com.lab422.vkanalyzer.utils.requests.VKUsersCommand
import com.lab422.vkanalyzer.utils.validator.UserNameValidator
import com.lab422.vkanalyzer.utils.viewState.ViewState
import com.lab422.vkanalyzer.utils.vkModels.user.User
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class MutualViewModel(
    private val navigator: Navigator,
    private val model: MutualFriendsModel?,
    private val dataProvider: FriendsListDataProvider,
    private val validator: UserNameValidator
) : ViewModel(), LifecycleObserver {

    private val state: MediatorLiveData<ViewState<List<RowDataModel<FriendsListType, *>>>> = MediatorLiveData()
    private val queryLiveData: MutableLiveData<String> = MutableLiveData()

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var rowData: MutableList<User> = mutableListOf()

    init {
        process()

        state.addSource(queryLiveData.debounce(300, viewModelScope)) {
            uiScope.launch {
                val data = dataProvider.filterByQuery(rowData, FriendsListType.Friends, it)
                state.postValue(ViewState(ViewState.Status.SUCCESS, data))
            }
        }
    }

    fun getState(): LiveData<ViewState<List<RowDataModel<FriendsListType, *>>>> = state

    fun onSearchQueryTyped(text: String) {
        queryLiveData.postValue(text.toLowerCase())
    }

    private fun process() {
        state.postValue(ViewState(ViewState.Status.LOADING))
        model?.let {
            val firstName = validator.validate(model.firstId)
            val secondName = validator.validate(model.secondId)

            if (validator.isId(firstName) && validator.isId(secondName)) {
                findMutualById(it.firstId, it.secondId)
            } else {
                getIdByUserName(firstName, secondName)
            }
        }
    }

    private fun getIdByUserName(firstName: String, secondName: String) {
        val isKnowFirstUserId: Boolean = validator.isId(firstName)
        val isKnowSecondUserId: Boolean = validator.isId(secondName)
        val nicknames = mutableListOf<String>()

        if (isKnowFirstUserId.not()) {
            nicknames.add(firstName)
        }

        if (isKnowSecondUserId.not()) {
            nicknames.add(secondName)
        }

        VK.execute(GetUserIdCommand(nicknames), object : VKApiCallback<List<String>> {
            override fun fail(error: Exception) {
                if (error is VKApiExecutionException) {
                    error.code
                }
                state.postValue(
                    ViewState(
                        status = ViewState.Status.ERROR,
                        error = error.message ?: "some error"
                    )
                )

                val eventProperties = JSONObject()
                try {
                    eventProperties.put("error", error.localizedMessage ?: "unknown error")
                } catch (exception: JSONException) {
                }
                Amplitude.getInstance().logEvent("failed load users id", eventProperties);
            }

            override fun success(result: List<String>) {
                if (result.isNullOrEmpty()) {
                    showError("Проверьте правильность полей")
                    return
                }
                val firstUserId = if (isKnowFirstUserId) firstName else result.first()
                val secondUserId = if (isKnowSecondUserId) secondName else {
                    if (result.size == 1) {
                        result.first()
                    } else {
                        result.last()
                    }
                }

                findMutualById(firstUserId, secondUserId)
            }
        })
    }

    private fun findMutualById(firstUserId: String, secondUserId: String) {
        VK.execute(VKUsersCommand(firstUserId, secondUserId), object : VKApiCallback<List<User>> {
            override fun fail(error: Exception) {
                if (error is VKApiExecutionException) {
                    error.code
                }
                state.postValue(
                    ViewState(
                        status = ViewState.Status.ERROR,
                        error = error.message ?: "some error"
                    )
                )
                val eventProperties = JSONObject()
                try {
                    eventProperties.put("error", error.localizedMessage ?: "unknown error")
                } catch (exception: JSONException) {
                }
                Amplitude.getInstance().logEvent("failed load mutual friends", eventProperties);
            }

            override fun success(result: List<User>) {
                onSuccessLoadUsers(result)
            }
        })
    }

    private fun showError(textError: String) {
        state.postValue(
            ViewState(
                status = ViewState.Status.ERROR,
                error = textError
            )
        )
    }

    private fun onSuccessLoadUsers(result: List<User>) {
        rowData.clear()
        rowData.addAll(result)

        val data = dataProvider.generateFriendsListData(rowData, FriendsListType.Friends)
        if (data.isEmpty()) {
            showError("Нет общих друзей")
        } else {
            state.postValue(ViewState(ViewState.Status.SUCCESS, data))
        }
        Amplitude.getInstance().logEvent("success load mutual friends");
    }
}