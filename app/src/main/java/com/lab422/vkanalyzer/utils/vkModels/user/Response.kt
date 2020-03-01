package com.lab422.vkanalyzer.utils.vkModels.user

data class User(
    val can_access_closed: Boolean,
    val first_name: String,
    val id: Long,
    val is_closed: Boolean,
    val last_name: String,
    val online: Int,
    val photo_200: String
)