package com.lab422.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lab422.analyzerapi.PhotosApi
import com.lab422.analyzerapi.models.photos.PhotosItemResponse
import com.lab422.common.viewState.ViewState
import com.lab422.interactor.model.UserPhotoModel

class PhotosInteractor constructor(
    private val photosApi: PhotosApi
) : BaseInteractor() {

    suspend fun getPhotosByLocation(lat: Long, long: Long): LiveData<ViewState<List<UserPhotoModel>>> = invokeBlock {
        val liveData = MutableLiveData<ViewState<List<UserPhotoModel>>>()
        val photosList = mutableListOf<UserPhotoModel>()
        val result = photosApi.getPhotosByLocation(lat.toString(), long.toString())
        result.response.items.forEach { photosList.add(it.convertToUserPhotoModel()) }
        liveData.postValue(ViewState(ViewState.Status.SUCCESS, photosList))

        return@invokeBlock liveData
    }
}

private fun PhotosItemResponse.convertToUserPhotoModel(): UserPhotoModel =
    UserPhotoModel(
        owner_id.toLong(),
        sizes.last().url,
        date.toLong()
    )