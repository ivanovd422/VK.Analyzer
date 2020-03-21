package com.lab422.vkanalyzer.utils.vkModels.user

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    val can_access_closed: Boolean,
    val first_name: String,
    val id: Long,
    val is_closed: Boolean,
    val last_name: String,
    val online: Int,
    @SerializedName("photo_200") val photoUrl: String?
) : Serializable