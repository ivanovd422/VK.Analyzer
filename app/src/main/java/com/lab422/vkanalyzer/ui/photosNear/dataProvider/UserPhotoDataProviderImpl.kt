package com.lab422.vkanalyzer.ui.photosNear.dataProvider

import com.lab422.interactor.model.UserPhotoData
import com.lab422.vkanalyzer.ui.base.Rawable
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.photosNear.adapter.UserPhotoRowType
import com.lab422.vkanalyzer.ui.photosNear.adapter.model.DatePhotosModel
import com.lab422.vkanalyzer.ui.photosNear.adapter.model.UserPhotoCellModel
import com.lab422.vkanalyzer.ui.photosNear.adapter.model.UserPhotoRowModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

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
        var rowDate: Long? = null

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

                if (userPhotoRows.isEmpty() || (rowDate != null && isTheSameDays(rowDate!!, userPhotoData.date).not())) {
                    userPhotoRows.add(
                        DateRowData(
                            UserPhotoRowType.Date,
                            DatePhotosModel(convertTimestampToHumanDate(userPhotoData.date * 1000))
                        )
                    )
                    rowDate = userPhotoData.date
                }

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
            userPhotoRows.add(
                LoadingRowData(
                    UserPhotoRowType.Loading
                )
            )
        }

        return userPhotoRows
    }

    private fun convertTimestampToHumanDate(unix: Long): String =
        try {
            val tz: TimeZone = TimeZone.getTimeZone("UTC")
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            sdf.timeZone = tz;
            val date = Date(unix)
            sdf.format(date)
        } catch (e: Exception) {
            ""
        }

    private fun isTheSameDays(firstTime: Long, secondTime: Long): Boolean {
        val firstDate = Date()
        val secondDate = Date()

        firstDate.time = firstTime * 1000
        secondDate.time = secondTime * 1000

        val diffInDays =
            (firstDate.time - secondDate.time) / (1000 * 60 * 60 * 24)

        return diffInDays < 1
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

@Suppress("FunctionName")
fun <T : Rawable> DateRowData(rowType: T, model: DatePhotosModel): RowDataModel<T, Any> {
    return RowDataModel(rowType, "Date-${model.date}", model.date)
}