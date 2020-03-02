package com.lab422.vkanalyzer.utils.requests

import com.lab422.vkanalyzer.utils.requests.vkParsers.MutualFriendsParser
import com.lab422.vkanalyzer.utils.requests.vkParsers.UserParser
import com.lab422.vkanalyzer.utils.vkModels.user.User
import com.lab422.vkanalyzer.utils.vkModels.user.UserResponse
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import kotlinx.coroutines.runBlocking


class VKUsersCommand(
    private val firstId: String,
    private val secondId: String
) : ApiCommand<List<User>>() {

    companion object {
        private const val REQUEST_NAME_GET_MUTUAL = "friends.getMutual"
        private const val PARAMETER_NAME_SOURCE_UID = "source_uid"
        private const val PARAMETER_NAME_TARGET_UID = "target_uid"

        private const val REQUEST_NAME_USER_GET = "users.get"
        private const val PARAMETER_NAME_USER_IDS = "user_ids"
        private const val PARAMETER_KEY_NAME = "fields"
        private const val PARAMETER_NAME_PHOTO = "photo_200"
        private const val PARAMETER_NAME_STATUS = "online"
    }

    override fun onExecute(manager: VKApiManager): List<User> = runBlocking {
        val mutualFriends = getMutualFriends(manager, firstId, secondId)
        getUsersList(manager, mutualFriends).response
    }

    private fun getMutualFriends(manager: VKApiManager, firstId: String, secondId: String): List<Long> {
        val call = VKMethodCall.Builder()
            .method(REQUEST_NAME_GET_MUTUAL)
            .args(PARAMETER_NAME_SOURCE_UID, firstId)
            .args(PARAMETER_NAME_TARGET_UID, secondId)
            .version(manager.config.version)
            .build()

        val result = manager.execute(call, MutualFriendsParser())
        return result.response
    }

    private fun getUsersList(manager: VKApiManager, ids: List<Long>): UserResponse {
        val idString = ids.joinToString(separator = ",")

        val call = VKMethodCall.Builder()
            .method(REQUEST_NAME_USER_GET)
            .args(PARAMETER_NAME_USER_IDS, idString)
            .args(PARAMETER_KEY_NAME, PARAMETER_NAME_PHOTO)
            .args(PARAMETER_KEY_NAME, PARAMETER_NAME_STATUS)
            .version(manager.config.version)
            .build()

        return manager.execute(call, UserParser())
    }
}