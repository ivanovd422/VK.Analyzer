package com.lab422.interactor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lab422.analyzerapi.PhotosApi
import com.lab422.analyzerapi.models.photos.PhotosItemResponse
import com.lab422.common.viewState.ViewState
import com.lab422.interactor.model.UserPhotoData
import com.lab422.interactor.model.UserPhotoResponse

class PhotosInteractor constructor(
    private val photosApi: PhotosApi
) : BaseInteractor() {

    suspend fun getPhotosByLocation(lat: String, long: String, offset: String, radius: String): LiveData<ViewState<UserPhotoResponse>> = invokeBlock {
        Log.d("tag", "getPhotosByLocation")
        val liveData = MutableLiveData<ViewState<UserPhotoResponse>>()
        val photosList = mutableListOf<UserPhotoData>()
        val result = photosApi.getPhotosByLocation(lat, long, offset, radius)
        result.response.items.forEach { photosList.add(it.convertToUserPhotoModel()) }
        val count = result.response.count

        liveData.postValue(
            ViewState(
                ViewState.Status.SUCCESS, UserPhotoResponse(
                    photosList,
                    count
                )
            )
        )

        return@invokeBlock liveData
    }
}

private fun PhotosItemResponse.convertToUserPhotoModel(): UserPhotoData =
    UserPhotoData(
        owner_id,
        sizes.last().url,
        date.toLong(),
        user_id,
        lat,
        long
    )