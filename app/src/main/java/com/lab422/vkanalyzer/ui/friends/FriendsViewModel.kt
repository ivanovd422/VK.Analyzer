package com.lab422.vkanalyzer.ui.friends

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider.FriendsListDataProvider
import com.lab422.vkanalyzer.utils.requests.FriendsCommand
import com.lab422.vkanalyzer.utils.viewState.ViewState
import com.lab422.vkanalyzer.utils.vkModels.user.User
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException

class FriendsViewModel(
    private val dataProvider: FriendsListDataProvider
) : ViewModel(), LifecycleObserver {

    private val state: MutableLiveData<ViewState<List<RowDataModel<FriendsListType, *>>>> = MutableLiveData()

    init {
        startLoadingFriendsList()
    }

    fun getFriendsState(): LiveData<ViewState<List<RowDataModel<FriendsListType, *>>>> = state

    private fun startLoadingFriendsList() {
        VK.execute(FriendsCommand(), object : VKApiCallback<List<User>> {
            override fun fail(error: Exception) {
                if (error is VKApiExecutionException) {
                    error.code
                }
                showError(error.message ?: "some error")
            }

            override fun success(result: List<User>) {
                val data = dataProvider.generateFriendsListData(result, FriendsListType.SelectableFriends)
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