package com.lab422.vkanalyzer.ui.photosNear

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lab422.common.viewState.ViewState
import com.lab422.interactor.PhotosInteractor
import com.lab422.vkanalyzer.ui.base.BaseViewModel



class PhotosNearViewModel(
    private val photosInteractor: PhotosInteractor
) : BaseViewModel(), LifecycleObserver {

    private val locationStateAvailability: MutableLiveData<ViewState<Boolean>> = MutableLiveData()
    fun getLocationStateAvailability(): LiveData<ViewState<Boolean>> = locationStateAvailability

    init {
        locationStateAvailability.value = ViewState(
            status = ViewState.Status.SUCCESS,
            data = false
        )
    }

    fun onCurrentLocationReceived(lat: String, long: String) {
        Log.d("tag", "lat - $lat")
        Log.d("tag", "long - $long")

    }

    fun onPermissionsGranted(isPermissionGranted: Boolean) {
        locationStateAvailability.value = ViewState(
            status = ViewState.Status.SUCCESS,
            data = isPermissionGranted
        )
    }
}