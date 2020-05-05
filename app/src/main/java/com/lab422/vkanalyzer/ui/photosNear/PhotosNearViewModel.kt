package com.lab422.vkanalyzer.ui.photosNear

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
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import com.lab422.vkanalyzer.utils.extensions.debounce
import kotlinx.coroutines.cancelChildren


class PhotosNearViewModel(
    private val photosInteractor: PhotosInteractor,
    private val dataProvider: UserPhotoDataProvider,
    private val tracker: TrackerService
) : BaseViewModel(), LifecycleObserver {

    private companion object {
        const val START_OFFSET = 100
        const val MAX_OFFSET = 3000

        val radiusList: List<Int> = listOf(800, 3000, 6000, 50000)
    }

    private val userFetchingLiveData: MutableLiveData<Pair<String, String>> = MutableLiveData()
    private var userPhotosCount = 0
    private var rawData: MutableList<UserPhotoData> = mutableListOf()
    private var currentLat = String()
    private var currentLong = String()
    private var offset: Int = START_OFFSET
    private var currentRadius = radiusList.first()

    private val locationStateAvailability: MutableLiveData<ViewState<Boolean>> = MutableLiveData()
    fun getLocationStateAvailability(): LiveData<ViewState<Boolean>> = locationStateAvailability

    private val userPhotosData: MediatorLiveData<
        ViewState<List<RowDataModel<UserPhotoRowType, *>>>
        > = MediatorLiveData()
    fun getUserPhotosDataState(): LiveData<ViewState<List<RowDataModel<UserPhotoRowType, *>>>> = userPhotosData

    private val coordinatesState: MediatorLiveData<Boolean> = MediatorLiveData()
    fun isCoordinatesExist(): LiveData<Boolean> = coordinatesState

    init {
        locationStateAvailability.value = ViewState(
            status = ViewState.Status.SUCCESS,
            data = false
        )

        userPhotosData.postValue(ViewState(ViewState.Status.LOADING))

        userFetchingLiveData
            .debounce(1000, viewModelScope)
            .switchMap { coordinates ->
            launchOnViewModelScope {
                photosInteractor.getPhotosByLocation(
                    coordinates.first,
                    coordinates.second,
                    offset.toString(),
                    currentRadius.toString()
                )
            }
        }.observeForever {
            val result = it.data
            if (it.isSuccess() && result != null) {
                val photoList = result.userPhotosData
                userPhotosCount = result.count
                offset += photoList.size
                rawData.addAll(photoList)
                val data = dataProvider.generateUserPhotoData(rawData, isEnoughLoaded())

                if (data.isEmpty()) {
                    if (currentRadius == radiusList.last()) {
                        tracker.loadPhotoNearby(false, errorMessage = it.error)
                        userPhotosData.postValue(ViewState(ViewState.Status.ERROR, error = it.error))
                    } else {
                        repeatSearchWithIncreasedRadius()
                    }
                } else {
                    tracker.loadPhotoNearby(true, data.size)
                    userPhotosData.postValue(ViewState(ViewState.Status.SUCCESS, data))
                }
            } else {
                tracker.loadPhotoNearby(false, errorMessage = it.error)
                userPhotosData.postValue(ViewState(ViewState.Status.ERROR, error = it.error))
            }
        }

        coordinatesState.value = false
    }

    fun onCoordinatesReceived(lat: String, long: String) {
        tracker.coordinatesReceived(lat, long)
        currentLat = lat
        currentLong = long
        coordinatesState.value = true
        userFetchingLiveData.value = Pair(currentLat, currentLong)
    }

    fun onReloadClicked() {
        if (currentLat.isEmpty() || currentLong.isEmpty()) return
        networkContext.cancelChildren()
        rawData.clear()
        rawData = mutableListOf()
        offset = START_OFFSET
        currentRadius = radiusList.first()
        userFetchingLiveData.value = Pair(currentLat, currentLong)
    }

    fun onNextPhotosLoad() {
        if (currentLat.isEmpty() || currentLong.isEmpty()) return
        userFetchingLiveData.value = Pair(currentLat, currentLong)
    }

    fun onPermissionsGranted(isPermissionGranted: Boolean) {
        locationStateAvailability.value = ViewState(
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
            userFetchingLiveData.value = Pair(currentLat, currentLong)
        }
    }

    private fun isEnoughLoaded(): Boolean = rawData.size < userPhotosCount && offset < MAX_OFFSET
}