package com.lab422.vkanalyzer.utils.requests

import com.google.gson.Gson
import com.lab422.vkanalyzer.utils.vkModels.mutual.MutualFriendsList
import com.lab422.vkanalyzer.utils.vkModels.user.User
import com.lab422.vkanalyzer.utils.vkModels.user.UserResponse
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import kotlinx.coroutines.runBlocking


class VKUsersCommand(
    private val firstId: String,
    private val secondId: String
) : ApiCommand<List<User>>() {

    override fun onExecute(manager: VKApiManager): List<User> = runBlocking {
        val mutualFriends = getMutualFriends(manager, firstId, secondId)
        getUsersList(manager, mutualFriends).response
    }

    private fun getMutualFriends(manager: VKApiManager, firstId: String, secondId: String): List<Long> {
        val call = VKMethodCall.Builder()
            .method("friends.getMutual")
            .args("source_uid", firstId)
            .args("target_uid", secondId)
            .version(manager.config.version)
            .build()

        val result = manager.execute(call, MutualFriendsParser())
        return result.response
    }

    private fun getUsersList(manager: VKApiManager, ids: List<Long>): UserResponse {
        val idString = ids.joinToString(separator = ",")

        val call = VKMethodCall.Builder()
            .method("users.get")
            .args("user_ids", idString)
            .args("fields", "photo_200")
            .args("fields", "online")
            .version(manager.config.version)
            .build()

        return manager.execute(call, UserResponseParser())
    }
}

private class UserResponseParser : VKApiResponseParser<UserResponse> {
    override fun parse(response: String): UserResponse {
        return Gson().fromJson(response, UserResponse::class.java)
    }
}

private class MutualFriendsParser : VKApiResponseParser<MutualFriendsList> {
    override fun parse(response: String): MutualFriendsList {
        return Gson().fromJson(response, MutualFriendsList::class.java)
    }
}

internal inline fun <reified T> parseGeneric(value: String): T {
    return Gson().fromJson(value, T::class.java)
}