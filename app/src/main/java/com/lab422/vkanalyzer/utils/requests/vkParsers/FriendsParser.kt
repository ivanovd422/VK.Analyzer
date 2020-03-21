package com.lab422.vkanalyzer.utils.requests.vkParsers

import com.google.gson.Gson
import com.lab422.vkanalyzer.utils.vkModels.friends.FriendsResponse
import com.vk.api.sdk.VKApiResponseParser

internal class FriendsParser : VKApiResponseParser<FriendsResponse> {
    override fun parse(response: String): FriendsResponse {
        return Gson().fromJson(response, FriendsResponse::class.java)
    }
}