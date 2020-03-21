package com.lab422.vkanalyzer.ui.mutualFriends

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider.FriendsListDataProvider
import com.lab422.vkanalyzer.ui.mutualFriends.model.MutualFriendsModel
import com.lab422.vkanalyzer.utils.navigator.Navigator
import com.lab422.vkanalyzer.utils.requests.GetUserIdCommand
import com.lab422.vkanalyzer.utils.requests.VKUsersCommand
import com.lab422.vkanalyzer.utils.validator.UserNameValidator
import com.lab422.vkanalyzer.utils.viewState.ViewState
import com.lab422.vkanalyzer.utils.vkModels.user.User
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException

class MutualViewModel(
    private val navigator: Navigator,
    private val model: MutualFriendsModel?,
    private val dataProvider: FriendsListDataProvider,
    private val validator: UserNameValidator
) : ViewModel(), LifecycleObserver {

    private val state: MutableLiveData<ViewState<List<RowDataModel<FriendsListType, *>>>> = MutableLiveData()

    init {
        process()
    }

    fun getState(): LiveData<ViewState<List<RowDataModel<FriendsListType, *>>>> = state

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
            }

            override fun success(result: List<User>) {
                val data = dataProvider.generateFriendsListData(result, FriendsListType.Friends)
                if (data.isEmpty()) {
                    showError("Нет общих друзей")
                } else {
                    state.postValue(ViewState(ViewState.Status.SUCCESS, data))
                }
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
}