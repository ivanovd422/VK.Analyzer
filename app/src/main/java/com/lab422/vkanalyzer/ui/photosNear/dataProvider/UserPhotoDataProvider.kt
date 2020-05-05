package com.lab422.vkanalyzer.ui.photosNear.dataProvider

import com.lab422.interactor.model.UserPhotoData
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.photosNear.adapter.UserPhotoRowType

interface UserPhotoDataProvider {
    fun generateUserPhotoData(
        userPhotoList: List<UserPhotoData>,
        shouldShowLoading: Boolean
    ): List<RowDataModel<UserPhotoRowType, *>>
}