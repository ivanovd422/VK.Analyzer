package com.lab422.vkanalyzer.ui.friends

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider.FriendsListDataProvider
import com.lab422.vkanalyzer.utils.extensions.debounce
import com.lab422.vkanalyzer.utils.requests.FriendsCommand
import com.lab422.vkanalyzer.utils.viewState.ViewState
import com.lab422.vkanalyzer.utils.vkModels.user.User
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch



class FriendsViewModel(
    private val dataProvider: FriendsListDataProvider
) : ViewModel(), LifecycleObserver {

    private val state: MediatorLiveData<ViewState<List<RowDataModel<FriendsListType, *>>>> = MediatorLiveData()
    private val queryLiveData: MutableLiveData<String> = MutableLiveData()

    private var rowData: MutableList<User> = mutableListOf()

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        startLoadingFriendsList()

        state.addSource(queryLiveData.debounce(300, viewModelScope)) {
            uiScope.launch {
                val data = dataProvider.filterByQuery(rowData, FriendsListType.SelectableFriends, it)
                state.postValue(ViewState(ViewState.Status.SUCCESS, data))
            }
        }
    }

    fun getFriendsState(): LiveData<ViewState<List<RowDataModel<FriendsListType, *>>>> = state

    fun onSearchQueryTyped(text: String) {
        queryLiveData.postValue(text.toLowerCase())
    }

    private fun startLoadingFriendsList() {
        VK.execute(FriendsCommand(), object : VKApiCallback<List<User>> {
            override fun fail(error: Exception) {
                if (error is VKApiExecutionException) {
                    error.code
                }
                showError(error.message ?: "some error")
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

    override fun onCleared() {
        super.onCleared()
        state.removeSource(queryLiveData)
        viewModelJob.cancel()
    }

    private fun onSuccessLoadUsers(result: List<User>) {
        rowData.clear()
        rowData.addAll(result)

        val data = dataProvider.generateFriendsListData(rowData, FriendsListType.SelectableFriends)
        if (data.isEmpty()) {
            showError("Нет общих друзей")
        } else {
            state.postValue(ViewState(ViewState.Status.SUCCESS, data))
        }
    }
}