package com.lab422.interactor

import com.lab422.analyzerapi.NetworkResponse
import com.lab422.analyzerapi.PhotosApi
import com.lab422.analyzerapi.map
import com.lab422.analyzerapi.models.photos.PhotosItemResponse
import com.lab422.interactor.model.UserPhotoData
import com.lab422.interactor.model.UserPhotoResponse

class PhotosInteractor constructor(
    private val photosApi: PhotosApi
) {

    suspend fun getPhotosByLocation(
        lat: String,
        long: String,
        offset: String,
        radius: String
    ): NetworkResponse<UserPhotoResponse> =
        photosApi.getPhotosByLocation(lat, long, offset, radius).map { photosResponse ->
            UserPhotoResponse(
                photosResponse.response.items.map { it.convertToUserPhotoModel() },
                photosResponse.response.count,
            )
        }
}

private fun PhotosItemResponse.convertToUserPhotoModel(): UserPhotoData =
    UserPhotoData(
        owner_id,
        sizes.last().url,
        date,
        user_id,
        lat,
        long,
        date
    )
