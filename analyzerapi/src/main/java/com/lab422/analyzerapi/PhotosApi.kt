package com.lab422.analyzerapi

import com.lab422.analyzerapi.models.photos.PhotosResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosApi {

    @GET("photos.search")
    suspend fun getPhotosByLocation(
        @Query("lat") lat: String,
        @Query("long") long: String
    ): PhotosResponse
}