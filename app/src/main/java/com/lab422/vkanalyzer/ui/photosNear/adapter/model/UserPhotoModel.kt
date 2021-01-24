package com.lab422.vkanalyzer.ui.photosNear.adapter.model

import com.lab422.vkanalyzer.ui.base.Diffable

data class UserPhotoRowModel(
    val userPhotosCells: List<UserPhotoCellModel>
) : Diffable {

    override fun isSame(same: Diffable): Boolean {
        if (same !is UserPhotoRowModel) {
            return false
        }
        return same.userPhotosCells == userPhotosCells
    }

    override fun isContentSame(same: Diffable): Boolean {
        if (same !is UserPhotoRowModel) {
            return false
        }
        return same.userPhotosCells == userPhotosCells
    }
}

data class UserPhotoCellModel(
    val userId: Int,
    val date: Long,
    val photoUrl: String,
    val lat: Double?,
    val long: Double?
) : Diffable {

    override fun isSame(same: Diffable): Boolean {
        if (same !is UserPhotoCellModel) {
            return false
        }
        return same.userId == userId &&
            same.date == date &&
            same.photoUrl == photoUrl &&
            same.lat == lat &&
            same.long == long
    }

    override fun isContentSame(same: Diffable): Boolean {
        if (same !is UserPhotoCellModel) {
            return false
        }
        return same.userId == userId &&
            same.date == date &&
            same.photoUrl == photoUrl &&
            same.lat == lat &&
            same.long == long
    }
}
