package com.lab422.vkanalyzer.ui.mutualFriends

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.MutualFriendsType
import com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider.MutualFriendsDataProvider
import com.lab422.vkanalyzer.ui.mutualFriends.model.MutualFriendsModel
import com.lab422.vkanalyzer.ui.mutualFriends.model.UserViewModel
import com.lab422.vkanalyzer.utils.navigator.Navigator
import com.lab422.vkanalyzer.utils.requests.VKUsersCommand
import com.lab422.vkanalyzer.utils.viewState.ViewState
import com.lab422.vkanalyzer.utils.vkModels.user.User
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException

class MutualViewModel(
    private val navigator: Navigator,
    private val model: MutualFriendsModel?,
    private val dataProvider: MutualFriendsDataProvider
) : ViewModel(), LifecycleObserver {

    private val state: MutableLiveData<ViewState<List<RowDataModel<MutualFriendsType, *>>>> = MutableLiveData()

    init {
        process()
    }

    fun getState(): LiveData<ViewState<List<RowDataModel<MutualFriendsType, *>>>> = state

    fun retryLoading() {
        process()
    }

    private fun process() {
        state.postValue(ViewState(ViewState.Status.LOADING))
        model?.let {
            findMutualById(it.firstId.toString(), it.secondId.toString())
        }
    }

    private fun findMutualById(firstUserId: String, secondUserId: String) {
        VK.execute(VKUsersCommand(firstUserId, secondUserId), object : VKApiCallback<List<User>> {
            override fun fail(error: Exception) {
                if (error is VKApiExecutionException) {
                    error.code
                }
                Log.d("tag", error.message ?: "")
                state.postValue(
                    ViewState(
                        status = ViewState.Status.ERROR,
                        error = "some error"
                    )
                )
            }

            override fun success(result: List<User>) {
                Log.d("tag", "success - $result")
                val data = dataProvider.generateMutualFriendsData(result)
                state.postValue(ViewState(ViewState.Status.SUCCESS, data))
            }
        })
    }
}