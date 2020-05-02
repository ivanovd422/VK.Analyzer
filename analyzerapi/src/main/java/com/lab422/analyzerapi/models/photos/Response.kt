package com.lab422.analyzerapi.models.photos

data class Response(
    val count: Int,
    val items: List<PhotosItemResponse>
)