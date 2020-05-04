package com.lab422.analyzerapi.models.photos

data class PhotosItemResponse(
    val album_id: Int,
    val date: Long,
    val id: Int,
    val lat: Double,
    val long: Double,
    val owner_id: Int,
    val post_id: Int,
    val sizes: List<Size>,
    val text: String,
    val user_id: Int?
)