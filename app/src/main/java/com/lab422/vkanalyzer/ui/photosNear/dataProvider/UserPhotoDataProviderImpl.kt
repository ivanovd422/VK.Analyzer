package com.lab422.vkanalyzer.ui.photosNear.dataProvider

import com.lab422.interactor.model.UserPhotoData
import com.lab422.vkanalyzer.ui.base.Rawable
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.photosNear.adapter.UserPhotoRowType
import com.lab422.vkanalyzer.ui.photosNear.adapter.model.UserPhotoCellModel
import com.lab422.vkanalyzer.ui.photosNear.adapter.model.UserPhotoRowModel

internal class UserPhotoDataProviderImpl : UserPhotoDataProvider {

    private companion object {
        const val GROUP_ID = 100
    }

    override fun generateUserPhotoData(
        userPhotoList: List<UserPhotoData>,
        shouldShowLoading: Boolean
    ): List<RowDataModel<UserPhotoRowType, *>> {
        val userPhotoRows = mutableListOf<RowDataModel<UserPhotoRowType, *>>()
        val listSize = userPhotoList.size
        var userPhotoRowModel: MutableList<UserPhotoCellModel> = mutableListOf()

        userPhotoList.filter {
            it.ownerId > 0 || (it.userId != null && it.userId != GROUP_ID)
        }.forEachIndexed { index, userPhotoData ->

            val userId = if (userPhotoData.ownerId > 0) userPhotoData.ownerId else userPhotoData.userId!!
            userPhotoRowModel.add(
                UserPhotoCellModel(
                    userId,
                    userPhotoData.photoPostDate.toString(),
                    userPhotoData.photoUrl,
                    userPhotoData.lat,
                    userPhotoData.long
                )
            )

            if (userPhotoRowModel.size == 3 || index == listSize) {
                userPhotoRows.add(
                    UserPhotoRowData(
                        UserPhotoRowType.UserPhotoRowType,
                        UserPhotoRowModel(userPhotoRowModel)
                    )
                )
                userPhotoRowModel = mutableListOf()
            }
        }

        if (shouldShowLoading && userPhotoRows.isNotEmpty()) {
            userPhotoRows.add(LoadingRowData(
                UserPhotoRowType.Loading
            ))
        }

        return userPhotoRows
    }
}


@Suppress("FunctionName")
fun <T : Rawable> UserPhotoRowData(rowType: T, p: UserPhotoRowModel): RowDataModel<T, Any> {
    return RowDataModel(rowType, "UserPhotoRowModel-{${p.hashCode()}}", p)
}

@Suppress("FunctionName")
fun <T : Rawable> LoadingRowData(rowType: T): RowDataModel<T, Any> {
    return RowDataModel(rowType, "Loading", null)
}