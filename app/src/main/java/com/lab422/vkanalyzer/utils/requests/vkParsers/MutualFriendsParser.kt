package com.lab422.vkanalyzer.utils.requests.vkParsers

import com.google.gson.Gson
import com.lab422.vkanalyzer.utils.vkModels.mutual.MutualFriendsList
import com.vk.api.sdk.VKApiResponseParser


internal class MutualFriendsParser : VKApiResponseParser<MutualFriendsList> {
    override fun parse(response: String): MutualFriendsList {
        return Gson().fromJson(response, MutualFriendsList::class.java)
    }
}