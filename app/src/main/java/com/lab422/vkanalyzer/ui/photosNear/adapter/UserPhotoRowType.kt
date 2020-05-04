package com.lab422.vkanalyzer.ui.photosNear.adapter

import com.lab422.vkanalyzer.ui.base.Diffable
import com.lab422.vkanalyzer.ui.base.Rawable

enum class UserPhotoRowType : Rawable, Diffable {
    UserPhoto,
    Loading,
    Date;

    override val rawValue: Int = this.ordinal

    override fun isContentSame(same: Diffable): Boolean {
        if (same !is UserPhotoRowType) {
            return false
        }
        return same.rawValue == rawValue
    }

    override fun isSame(same: Diffable): Boolean {
        if (same !is UserPhotoRowType) {
            return false
        }

        return (same.rawValue == rawValue) ||
            (sameTypes.contains(same.rawValue) && sameTypes.contains(rawValue))
    }
}

private val sameTypes = listOf(
    UserPhotoRowType.UserPhoto.rawValue,
    UserPhotoRowType.Loading.rawValue,
    UserPhotoRowType.Date.rawValue
)