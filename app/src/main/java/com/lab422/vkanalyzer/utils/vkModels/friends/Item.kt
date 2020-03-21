package com.lab422.vkanalyzer.utils.vkModels.friends

import com.lab422.vkanalyzer.utils.vkModels.user.User

data class Item(
    val can_access_closed: Boolean,
    val first_name: String,
    val id: Long,
    val is_closed: Boolean,
    val last_name: String,
    val online: Int,
    val photo_50: String?,
    val track_code: String
)

fun Item.convertToUser(): User =
    User(
        can_access_closed,
        first_name,
        id,
        is_closed,
        last_name,
        online,
        photo_50
        )