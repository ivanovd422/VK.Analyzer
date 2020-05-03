package com.lab422.vkanalyzer.ui.photosNear

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.lab422.common.viewState.ViewState
import com.lab422.common.viewState.isSuccess
import com.lab422.interactor.PhotosInteractor
import com.lab422.interactor.model.UserPhotoData
import com.lab422.vkanalyzer.ui.base.BaseViewModel
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.photosNear.adapter.UserPhotoRowType
import com.lab422.vkanalyzer.ui.photosNear.dataProvider.UserPhotoDataProvider
import com.lab422.vkanalyzer.utils.extensions.debounce


class PhotosNearViewModel(
    private val photosInteractor: PhotosInteractor,
    private val dataProvider: UserPhotoDataProvider
) : BaseViewModel(), LifecycleObserver {

    private companion object {
        const val START_OFFSET = 100
        const val MAX_OFFSET = 3000

        val radiusList: List<Int> = listOf(10, 100, 800, 6000, 50000)
    }

    private val userFetchingLiveData: MutableLiveData<Unit> = MutableLiveData()
    private var userPhotosCount = 0
    private val rawData: MutableList<UserPhotoData> = mutableListOf()
    private var currentLat = String()
    private var currentLong = String()
    private var offset: Int = START_OFFSET
    private var currentRadius = radiusList.first()

    private val locationStateAvailability: MutableLiveData<ViewState<Boolean>> = MutableLiveData()
    fun getLocationStateAvailability(): LiveData<ViewState<Boolean>> = locationStateAvailability

    private val userPhotosData: MediatorLiveData<ViewState<List<RowDataModel<UserPhotoRowType, *>>>> =
        MediatorLiveData()

    fun getUserPhotosDataState(): LiveData<ViewState<List<RowDataModel<UserPhotoRowType, *>>>> = userPhotosData

    private val coordinatesState: MediatorLiveData<Boolean> = MediatorLiveData()
    fun isCoordinatesExist(): LiveData<Boolean> = coordinatesState

    init {
        locationStateAvailability.value = ViewState(
            status = ViewState.Status.SUCCESS,
            data = false
        )

        userFetchingLiveData.debounce(1000, viewModelScope).switchMap {
            launchOnViewModelScope {
                Log.d("tag", "launchOnViewModelScope")
                photosInteractor.getPhotosByLocation(
                    currentLat,
                    currentLong,
                    offset.toString(),
                    currentRadius.toString()
                )
            }
        }.observeForever {
            if (it.isSuccess()) {
                userPhotosCount = it.data?.count ?: 0
                onSuccessLoadUsers(it.data?.userPhotosData)
            } else {
                showError("some error")
            }
        }

        coordinatesState.value = false
    }

    fun onCoordinatesReceived(lat: String, long: String) {
        Log.d("tag", "onCoordinatesReceived")
        currentLat = lat
        currentLong = long
        coordinatesState.value = true
        userPhotosData.postValue(ViewState(ViewState.Status.LOADING, data = listOf()))
        userFetchingLiveData.value = Unit
    }

    fun onReloadClicked() {
        Log.d("tag", "onReloadClicked")
        if (currentLat.isEmpty() || currentLong.isEmpty()) return
        rawData.clear()
        offset = START_OFFSET
        currentRadius = radiusList.first()
        userPhotosData.postValue(ViewState(ViewState.Status.LOADING))
        userFetchingLiveData.value = Unit
    }

    fun onNextPhotosLoad() {
        Log.d("tag", "onNextPhotosLoad")
        if (currentLat.isEmpty() || currentLong.isEmpty()) return
        this.userFetchingLiveData.value = Unit
    }

    fun onPermissionsGranted(isPermissionGranted: Boolean) {
        locationStateAvailability.value = ViewState(
            status = ViewState.Status.SUCCESS,
            data = isPermissionGranted
        )
    }

    private fun onSuccessLoadUsers(result: List<UserPhotoData>?) {
        if (result == null) return
        offset += rawData.size
        rawData.addAll(result)

        val data = dataProvider.generateUserPhotoData(rawData, isEnoughLoaded())

        if (data.isEmpty()) {
            if (currentRadius == radiusList.last()) {
                showError("Нет фото рядом")
            } else {
                repeatSearchWithIncreasedRadius()
            }
        } else {
            userPhotosData.postValue(ViewState(ViewState.Status.SUCCESS, data))
        }
    }

    private fun showError(textError: String) {
        userPhotosData.postValue(
            ViewState(
                status = ViewState.Status.ERROR,
                error = textError
            )
        )
    }

    private fun repeatSearchWithIncreasedRadius() {
        var currentIndex = radiusList.indexOf(currentRadius)
        if (currentIndex < radiusList.size - 1) {
            currentIndex++
            currentRadius = radiusList[currentIndex]
            userFetchingLiveData.value = Unit
        }
    }

    private fun isEnoughLoaded(): Boolean = rawData.size < userPhotosCount && offset < MAX_OFFSET
}