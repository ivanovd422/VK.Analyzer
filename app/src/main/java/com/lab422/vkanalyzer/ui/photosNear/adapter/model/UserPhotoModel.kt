package com.lab422.vkanalyzer.ui.photosNear.adapter.model

import com.lab422.vkanalyzer.ui.base.Diffable
import com.lab422.vkanalyzer.ui.base.Rawable

data class UserPhotoRowModel(
    val userPhotosCells: List<UserPhotoCellModel>
) : Diffable, Rawable {

    override val rawValue: Int = this.hashCode()

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
    val date: String,
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