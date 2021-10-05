package com.lab422.vkanalyzer.ui.photosNear.dataProvider

import com.lab422.common.StringProvider
import com.lab422.interactor.model.UserPhotoData
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.base.Rawable
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.photosNear.adapter.UserPhotoRowType
import com.lab422.vkanalyzer.ui.photosNear.adapter.model.DatePhotosModel
import com.lab422.vkanalyzer.ui.photosNear.adapter.model.UserPhotoCellModel
import com.lab422.vkanalyzer.ui.photosNear.adapter.model.UserPhotoRowModel
import com.lab422.vkanalyzer.utils.extensions.daysFromToday
import com.lab422.vkanalyzer.utils.extensions.uiFormatted
import java.util.Calendar
import java.util.Date


internal class UserPhotoDataProviderImpl(
    private val stringProvider: StringProvider
) : UserPhotoDataProvider {

    private companion object {
        const val GROUP_ID = 100
        const val DATE_MULTIPLIER = 1000
        const val ROWS_COUNT = 3
    }

    override fun generateUserPhotoData(
        userPhotoList: List<UserPhotoData>,
        shouldShowLoading: Boolean
    ): List<RowDataModel<UserPhotoRowType, *>> {
        val finalUserPhotosList = mutableListOf<RowDataModel<UserPhotoRowType, *>>()
        val photos = mutableListOf<String>()
        val listSize: Int
        var userPhotoRowModel: MutableList<UserPhotoCellModel> = mutableListOf()
        var rowDate: Long? = null

        userPhotoList.asSequence()
            .filter { it.ownerId > 0 || (it.userId != null && it.userId != GROUP_ID) }
            .filter { photos.contains(it.photoUrl).not() }
            .map { photos.add(it.photoUrl); it }
            .toList()
            .also { listSize = it.size }
            .forEachIndexed { index, userPhotoData ->
                val userId = if (userPhotoData.ownerId > 0) userPhotoData.ownerId else userPhotoData.userId!!

                val photoCell = UserPhotoCellModel(
                    userId,
                    userPhotoData.photoPostDate,
                    userPhotoData.photoUrl,
                    userPhotoData.lat,
                    userPhotoData.long
                )

                // if it is first cycle or another day
                if (rowDate == null || isTheSameDays(rowDate!!, photoCell.date).not()) {

                    // check if list did not inserted then add it and clear list
                    if (userPhotoRowModel.isNotEmpty()) {
                        finalUserPhotosList.add(
                            UserPhotoRowData(
                                UserPhotoRowType.UserPhoto,
                                UserPhotoRowModel(userPhotoRowModel)
                            )
                        )
                        userPhotoRowModel = mutableListOf()
                    }

                    finalUserPhotosList.add(
                        DateRowData(
                            UserPhotoRowType.Date,
                            DatePhotosModel(convertTimestampToHumanDate(photoCell.date * DATE_MULTIPLIER))
                        )
                    )
                    rowDate = photoCell.date
                    userPhotoRowModel.add(photoCell)

                    if (index == listSize - 1) {
                        finalUserPhotosList.add(
                            UserPhotoRowData(
                                UserPhotoRowType.UserPhoto,
                                UserPhotoRowModel(userPhotoRowModel)
                            )
                        )
                        userPhotoRowModel = mutableListOf()
                    }
                }
                // the day is the same
                else {

                    if (userPhotoRowModel.size == ROWS_COUNT || index == listSize - 1) {
                        finalUserPhotosList.add(
                            UserPhotoRowData(
                                UserPhotoRowType.UserPhoto,
                                UserPhotoRowModel(userPhotoRowModel)
                            )
                        )
                        userPhotoRowModel = mutableListOf()
                    }
                    rowDate = photoCell.date
                    userPhotoRowModel.add(photoCell)
                }
            }

        if (shouldShowLoading && finalUserPhotosList.isNotEmpty()) {
            finalUserPhotosList.add(
                LoadingRowData(
                    UserPhotoRowType.Loading
                )
            )
        }

        return finalUserPhotosList
    }

    private fun convertTimestampToHumanDate(unix: Long): String =
        try {
            val date = Date(unix)
            val dayFromToday = date.daysFromToday
            var humanDate = date.uiFormatted(
                stringProvider.getString(R.string.photo_near_date_format),
                stringProvider
            )

            if (dayFromToday == 1L) {
                humanDate = "${stringProvider.getString(R.string.today)}, $humanDate"
            }

            if (dayFromToday == 0L) {
                humanDate = "${stringProvider.getString(R.string.yesterday)}, $humanDate"
            }

            humanDate
        } catch (e: Exception) {
            ""
        }

    private fun isTheSameDays(firstTime: Long, secondTime: Long): Boolean {
        val cal1: Calendar = Calendar.getInstance()
        val cal2: Calendar = Calendar.getInstance()
        val firstDate = Date()
        val secondDate = Date()

        firstDate.time = firstTime * DATE_MULTIPLIER
        secondDate.time = secondTime * DATE_MULTIPLIER

        cal1.time = firstDate
        cal2.time = secondDate

        return cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR] &&
            cal1[Calendar.YEAR] == cal2[Calendar.YEAR]
    }
}

@Suppress("FunctionName")
fun <T : Rawable> UserPhotoRowData(rowType: T, p: UserPhotoRowModel): RowDataModel<T, Any> {
    return RowDataModel(rowType, "UserPhotoRowModel-{${p.userPhotosCells}", p)
}

@Suppress("FunctionName")
fun <T : Rawable> LoadingRowData(rowType: T): RowDataModel<T, Any> {
    return RowDataModel(rowType, "Loading", null)
}

@Suppress("FunctionName")
fun <T : Rawable> DateRowData(rowType: T, model: DatePhotosModel): RowDataModel<T, Any> {
    return RowDataModel(rowType, "Date-${model.date}", model.date)
}
