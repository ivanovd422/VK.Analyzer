package com.lab422.vkanalyzer.ui.photosNear.userInfo.model

data class UserInfoModel(
    val userId: String,
    val userName: String,
    val userStatus: Boolean,
    val userPhotoUrl: String,
    val lat: Double?,
    val long: Double?
)