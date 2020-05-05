package com.lab422.vkanalyzer.ui.photosNear.adapter.model

import com.lab422.vkanalyzer.ui.base.Diffable

data class DatePhotosModel(
    val date: String
): Diffable {
    override fun isSame(same: Diffable): Boolean = false

    override fun isContentSame(same: Diffable): Boolean = false
}