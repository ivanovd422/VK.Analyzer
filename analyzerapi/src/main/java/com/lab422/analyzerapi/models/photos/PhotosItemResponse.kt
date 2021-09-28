package com.lab422.analyzerapi.models.photos

import com.google.gson.annotations.SerializedName

data class PhotosItemResponse(
    @SerializedName("album_id")
    val albumId: Int,
    val date: Long,
    val id: Int,
    val lat: Double,
    val long: Double,
    @SerializedName("owner_id")
    val ownerId: Int,
    @SerializedName("post_id")
    val postId: Int,
    val sizes: List<Size>,
    val text: String,
    @SerializedName("user_id")
    val userId: Int?
)
