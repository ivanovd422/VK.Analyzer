package com.lab422.vkanalyzer.ui.main

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lab422.vkanalyzer.utils.SingleLiveEvent
import com.lab422.vkanalyzer.utils.navigator.Navigator
import com.lab422.vkanalyzer.utils.requests.MutualFriendsRequest
import com.lab422.vkanalyzer.utils.requests.VKUsersCommand
import com.lab422.vkanalyzer.utils.settings.AppSettings
import com.lab422.vkanalyzer.utils.vkModels.MutualListId
import com.lab422.vkanalyzer.utils.vkModels.UserModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.exceptions.VKApiException
import com.vk.api.sdk.exceptions.VKApiExecutionException

class MainViewModel(
    private val navigator: Navigator
) : ViewModel(), LifecycleObserver {



    fun onSearchClicked(firstUserId: String, secondUserId: String) {
        findById(firstUserId, secondUserId)
    }

    private fun findById(firstUserId: String, secondUserId: String) {
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
    }
}