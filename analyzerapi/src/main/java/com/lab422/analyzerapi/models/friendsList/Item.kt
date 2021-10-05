package com.lab422.analyzerapi.models.friendsList

import com.google.gson.annotations.SerializedName
import com.lab422.analyzerapi.models.users.NewUser
import java.io.Serializable

data class Item(
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
    @SerializedName("photo_100")
    val photoUrl: String?,
    @SerializedName("track_code")
    val trackCode: String
) : Serializable

fun Item.convertToUser(): NewUser =
    NewUser(
        canAccessClosed,
        firstName,
        id,
        isClosed,
        lastName,
        online,
        photoUrl
    )
