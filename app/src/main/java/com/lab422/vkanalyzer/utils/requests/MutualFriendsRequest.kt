package com.lab422.vkanalyzer.utils.requests

import com.lab422.vkanalyzer.utils.vkModels.MutualListId
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import com.google.gson.Gson


class MutualFriendsRequest(firstUser: String, secondUser: String) : VKRequest<MutualListId>("friends.getMutual") {
    init {
        addParam("source_uid", firstUser)
        addParam("target_uid", secondUser)
    }

    override fun parse(r: JSONObject): MutualListId {
        return Gson().fromJson(r.toString(), MutualListId::class.java)
    }
}