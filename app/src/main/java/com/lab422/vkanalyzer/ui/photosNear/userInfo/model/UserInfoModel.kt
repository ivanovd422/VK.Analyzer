package com.lab422.vkanalyzer.ui.photosNear.userInfo.model

data class UserInfoModel(
    val userId: String,
    val userName: String,
    val userStatus: Boolean,
    val userAvatarPhotoUrl: String,
    val lat: Double?,
    val long: Double?,
    val clickedPhotoUrl: String
)