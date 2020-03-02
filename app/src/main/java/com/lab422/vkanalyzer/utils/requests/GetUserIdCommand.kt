package com.lab422.vkanalyzer.utils.requests

import com.lab422.vkanalyzer.utils.requests.vkParsers.UserParser
import com.lab422.vkanalyzer.utils.vkModels.user.UserResponse
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import kotlinx.coroutines.runBlocking


class GetUserIdCommand(
    private val userNicknames: List<String>
) : ApiCommand<List<String>>() {

    companion object {
        private const val REQUEST_NAME_USER_GET = "users.get"
        private const val PARAMETER_NAME_USER_IDS = "user_ids"
    }

    override fun onExecute(manager: VKApiManager): List<String> = runBlocking {
        getUsersList(manager, userNicknames).response.map {
            it.id.toString()
        }
    }

    private fun getUsersList(manager: VKApiManager, userNicknames: List<String>): UserResponse {
        val idString = userNicknames.joinToString(separator = ",")

        val call = VKMethodCall.Builder()
            .method(REQUEST_NAME_USER_GET)
            .args(PARAMETER_NAME_USER_IDS, idString)
            .version(manager.config.version)
            .build()

        return manager.execute(call, UserParser())
    }
}