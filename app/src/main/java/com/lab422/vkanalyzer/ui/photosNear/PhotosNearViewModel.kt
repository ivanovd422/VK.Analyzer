package com.lab422.vkanalyzer.ui.photosNear

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab422.analyzerapi.onResult
import com.lab422.common.viewState.ViewState
import com.lab422.interactor.PhotosInteractor
import com.lab422.interactor.model.UserPhotoData
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.photosNear.adapter.UserPhotoRowType
import com.lab422.vkanalyzer.ui.photosNear.dataProvider.UserPhotoDataProvider
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PhotosNearViewModel(
    private val photosInteractor: PhotosInteractor,
    private val dataProvider: UserPhotoDataProvider,
    private val tracker: TrackerService
) : ViewModel(), LifecycleObserver {

    private companion object {
        const val START_OFFSET = 100
        const val MAX_OFFSET = 3000

        val radiusList: List<Int> = listOf(800, 3000, 6000, 50000)
    }

    private var userPhotosCount = 0
    private var rawData: MutableList<UserPhotoData> = mutableListOf()
    private var currentLat = String()
    private var currentLong = String()
    private var offset: Int = START_OFFSET
    private var currentRadius = radiusList.first()

    private val _locationStateAvailability: MutableLiveData<ViewState<Boolean>> = MutableLiveData()
    val locationStateAvailability: LiveData<ViewState<Boolean>> = _locationStateAvailability

    private val _userPhotosData: MutableLiveData<
        ViewState<List<RowDataModel<UserPhotoRowType, *>>>
        > = MutableLiveData()

    val userPhotosData: LiveData<ViewState<List<RowDataModel<UserPhotoRowType, *>>>> = _userPhotosData

    private val _coordinatesState: MutableLiveData<Boolean> = MutableLiveData(false)
    val coordinatesState: LiveData<Boolean> = _coordinatesState

    private var loadingJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        loadingJob?.cancel()
    }

    fun onCoordinatesReceived(lat: String, long: String) {
        tracker.coordinatesReceived(lat, long)
        currentLat = lat
        currentLong = long
        _coordinatesState.value = true
        startLoading(currentLat, currentLong)
    }

    fun onReloadClicked() {
        if (currentLat.isEmpty() || currentLong.isEmpty()) return
        rawData.clear()
        rawData = mutableListOf()
        offset = START_OFFSET
        currentRadius = radiusList.first()
        startLoading(currentLat, currentLong)
    }

    fun onNextPhotosLoad() {
        if (currentLat.isEmpty() || currentLong.isEmpty()) return
        startLoading(currentLat, currentLong)
    }

    fun onPermissionsGranted(isPermissionGranted: Boolean) {
        _locationStateAvailability.value = ViewState(
            status = ViewState.Status.SUCCESS,
            data = isPermissionGranted
        )
    }

    private fun repeatSearchWithIncreasedRadius() {
        if (currentLat.isEmpty() || currentLong.isEmpty()) return
        var currentIndex = radiusList.indexOf(currentRadius)
        if (currentIndex < radiusList.size - 1) {
            currentIndex++
            currentRadius = radiusList[currentIndex]
            startLoading(currentLat, currentLong)
        }
    }

    private fun startLoading(lat: String, long: String) {
        _userPhotosData.value = ViewState(ViewState.Status.LOADING)
        loadingJob?.cancel()
        loadingJob = viewModelScope.launch {
            photosInteractor.getPhotosByLocation(
                lat,
                long,
                offset.toString(),
                currentRadius.toString()
            ).onResult(
                isSuccess = { result ->
                    val photoList = result.userPhotosData
                    userPhotosCount = result.count
                    offset += photoList.size
                    rawData.addAll(photoList)
                    val data = dataProvider.generateUserPhotoData(rawData, isEnoughLoaded())

                    if (data.isEmpty()) {
                        if (currentRadius == radiusList.last()) {
                            val msg = "Список пуст"
                            tracker.loadPhotoNearby(false, errorMessage = msg)
                            _userPhotosData.value = ViewState(ViewState.Status.ERROR, error = msg)
                        } else {
                            repeatSearchWithIncreasedRadius()
                        }
                    } else {
                        tracker.loadPhotoNearby(true, data.size)
                        _userPhotosData.value = ViewState(ViewState.Status.SUCCESS, data)
                    }
                },
                isFailure = { error, _ ->
                    tracker.loadPhotoNearby(false, errorMessage = error.message)
                    _userPhotosData.value = ViewState(ViewState.Status.ERROR, error = error.message)
                }
            )
        }
    }

    private fun isEnoughLoaded(): Boolean = rawData.size < userPhotosCount && offset < MAX_OFFSET
}
