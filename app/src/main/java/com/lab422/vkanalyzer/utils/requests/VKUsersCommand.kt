package com.lab422.vkanalyzer.utils.requests

import com.google.gson.Gson
import com.lab422.vkanalyzer.utils.vkModels.UserModel
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand
import kotlinx.coroutines.runBlocking


class VKUsersCommand(
    private val firstId: String,
    private val secondId: String
) : ApiCommand<List<UserModel>>() {

    override fun onExecute(manager: VKApiManager): List<UserModel> = runBlocking {
        val result = mutableListOf<UserModel>()
        val firstList = getFriendsList(manager, firstId)
        result.add(firstList)
        val secondList = getFriendsList(manager, secondId)
        result.add(secondList)
        result
    }


    private class ResponseApiParser : VKApiResponseParser<UserModel> {
        override fun parse(response: String): UserModel {
            return Gson().fromJson(response, UserModel::class.java)
        }
    }

    private fun getFriendsList(manager: VKApiManager, id: String): UserModel {
        val call = VKMethodCall.Builder()
            .method("friends.get")
            .args("user_id", id)
            .args("fields", "photo_50")
            .args("fields", "online")
            .version(manager.config.version)
            .build()

        return manager.execute(call, ResponseApiParser())
    }
}