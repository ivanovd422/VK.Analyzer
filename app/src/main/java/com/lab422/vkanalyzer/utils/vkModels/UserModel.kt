package com.lab422.vkanalyzer.utils.vkModels

data class UserModel(
    val response: Response
)

data class Response(
    val count: Int,
    val items: List<Item>
)

data class Item(
    val can_access_closed: Boolean,
    val first_name: String,
    val id: Int,
    val is_closed: Boolean,
    val last_name: String,
    val online: Int,
    val online_app: Int,
    val online_mobile: Int,
    val photo_50: String,
    val track_code: String
)