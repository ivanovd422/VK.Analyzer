package com.lab422.analyzerapi.models.friendsList

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Item(
    val can_access_closed: Boolean,
    val first_name: String,
    val id: Long,
    val is_closed: Boolean,
    val last_name: String,
    val online: Int,
    @SerializedName("photo_100") val photoUrl: String?,
    val track_code: String
) : Serializable