package com.lab422.analyzerapi.models.users

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NewUser(
    @SerializedName("can_access_closed")
    val canAccessClosed: Boolean,
    @SerializedName("first_name")
    val firstName: String,
    val id: Long,
    @SerializedName("is_closed")
    val isClosed: Boolean,
    @SerializedName("last_name")
    val lastName: String,
    val online: Int,
    @SerializedName("photo_200") val photoUrl: String?
) : Serializable
