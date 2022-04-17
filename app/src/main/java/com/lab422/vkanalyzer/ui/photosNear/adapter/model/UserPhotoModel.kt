package com.lab422.vkanalyzer.ui.photosNear.adapter.model


data class UserPhotoRowModel(
    val userPhotosCells: List<UserPhotoCellModel>
)

data class UserPhotoCellModel(
    val userId: Int,
    val date: Long,
    val photoUrl: String,
    val lat: Double?,
    val long: Double?
)
