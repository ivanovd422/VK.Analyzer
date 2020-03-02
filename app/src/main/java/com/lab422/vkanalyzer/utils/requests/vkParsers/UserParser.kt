package com.lab422.vkanalyzer.utils.requests.vkParsers

import com.google.gson.Gson
import com.lab422.vkanalyzer.utils.vkModels.user.UserResponse
import com.vk.api.sdk.VKApiResponseParser

internal class UserParser : VKApiResponseParser<UserResponse> {
    override fun parse(response: String): UserResponse {
        return Gson().fromJson(response, UserResponse::class.java)
    }
}