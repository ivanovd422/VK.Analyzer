package com.lab422.vkanalyzer.ui.photosNear.adapter

import com.lab422.vkanalyzer.ui.base.Rawable

enum class UserPhotoRowType : Rawable {
    UserPhotoRowType,
    Loading;

    override val rawValue: Int = this.ordinal
}