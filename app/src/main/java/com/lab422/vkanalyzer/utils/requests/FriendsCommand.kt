package com.lab422.vkanalyzer.utils.requests

import com.lab422.vkanalyzer.utils.requests.vkParsers.UserParser
import com.lab422.vkanalyzer.utils.vkModels.user.User
import com.lab422.vkanalyzer.utils.vkModels.user.UserResponse
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import kotlinx.coroutines.runBlocking

class FriendsCommand : ApiCommand<List<User>>() {

    companion object {
        private const val REQUEST_NAME_USER_GET = "users.get"
        private const val PARAMETER_KEY_NAME = "fields"
        private const val PARAMETER_NAME_PHOTO = "photo_200"
        private const val PARAMETER_NAME_STATUS = "online"
    }

    override fun onExecute(manager: VKApiManager): List<User> = runBlocking {
        getUsersList(manager).response
    }

    private fun getUsersList(manager: VKApiManager): UserResponse {
        val call = VKMethodCall.Builder()
            .method(REQUEST_NAME_USER_GET)
            .args(PARAMETER_KEY_NAME, PARAMETER_NAME_PHOTO)
            .args(PARAMETER_KEY_NAME, PARAMETER_NAME_STATUS)
            .version(manager.config.version)
            .build()

        return manager.execute(call, UserParser())
    }
}