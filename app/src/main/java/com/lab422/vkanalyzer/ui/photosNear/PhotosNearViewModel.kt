package com.lab422.vkanalyzer.ui.photosNear

import androidx.lifecycle.LifecycleObserver
import com.lab422.interactor.PhotosInteractor
import com.lab422.vkanalyzer.ui.base.BaseViewModel

class PhotosNearViewModel(
    private val photosInteractor: PhotosInteractor
) : BaseViewModel(), LifecycleObserver {

}