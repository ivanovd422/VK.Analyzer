package com.lab422.interactor.model

data class UserPhotoData(
    val ownerId: Int,
    val photoUrl: String,
    val photoPostDate: Long,
    val userId: Int?,
    val lat: Double?,
    val long: Double?,
    val date: Long
)
