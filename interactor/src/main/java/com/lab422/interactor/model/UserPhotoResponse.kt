package com.lab422.interactor.model

data class UserPhotoResponse(
    val userPhotosData: List<UserPhotoData>,
    val count: Int
)