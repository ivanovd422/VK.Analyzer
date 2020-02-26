package com.lab422.vkanalyzer.ui.mutualFriends

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.lab422.vkanalyzer.ui.mutualFriends.model.MutualFriendsModel
import com.lab422.vkanalyzer.utils.navigator.Navigator

class MutualViewModel(
    private val navigator: Navigator,
    private val model: MutualFriendsModel?
) : ViewModel(), LifecycleObserver {


    init {
        Log.d("tag", "first id - " + model?.firstId)
    }

/*    private val state: MutableLiveData<ViewState<Unit>> = MutableLiveData()

    fun getState(): LiveData<ViewState<Unit>> = state


    fun onSearchClicked(firstUserId: String, secondUserId: String) {
        findById(firstUserId, secondUserId)
    }*/

/*    private fun findById(firstUserId: String, secondUserId: String) {
        VK.execute(VKUsersCommand("6492", "6492"), object : VKApiCallback<List<UserModel>> {
            override fun fail(error: Exception) {
                if (error is VKApiExecutionException) {
                    error.code
                }
                Log.d("tag", error?.message)
            }

            override fun success(result: List<UserModel>) {
                Log.d("tag", "success - $result")
            }
        })
        Log.d("tag", "test")
    }*/
}