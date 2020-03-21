package com.lab422.vkanalyzer.utils.requests

import com.lab422.vkanalyzer.utils.requests.vkParsers.FriendsParser
import com.lab422.vkanalyzer.utils.vkModels.friends.FriendsResponse
import com.lab422.vkanalyzer.utils.vkModels.friends.convertToUser
import com.lab422.vkanalyzer.utils.vkModels.user.User
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import kotlinx.coroutines.runBlocking

class FriendsCommand : ApiCommand<List<User>>() {

    companion object {
        private const val REQUEST_NAME_USER_GET = "friends.get"
        private const val PARAMETER_KEY_NAME = "fields"
        private const val PARAMETER_NAME_PHOTO = "photo_50"
        private const val PARAMETER_NAME_STATUS = "online"
    }

    override fun onExecute(manager: VKApiManager): List<User> = runBlocking {
        val friendsList = mutableListOf<User>()
        getUsersList(manager).response.items.forEach { friendsList.add(it.convertToUser()) }
        friendsList
    }

    private fun getUsersList(manager: VKApiManager): FriendsResponse {
        val call = VKMethodCall.Builder()
            .method(REQUEST_NAME_USER_GET)
            .args(PARAMETER_KEY_NAME, PARAMETER_NAME_PHOTO)
            .args(PARAMETER_KEY_NAME, PARAMETER_NAME_STATUS)
            .version(manager.config.version)
            .build()

        return manager.execute(call, FriendsParser())
    }
}