package com.lab422.vkanalyzer.ui.photosNear.dataProvider

import com.lab422.interactor.model.UserPhotoData

interface UserPhotoDataProvider {
    fun generateUserPhotoData(
        userPhotoList: List<UserPhotoData>,
        shouldShowLoading: Boolean
    ): List<Any>

    fun generateShimmerUserPhotoData(): List<Any>
}
