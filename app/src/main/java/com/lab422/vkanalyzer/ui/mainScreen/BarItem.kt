package com.lab422.vkanalyzer.ui.mainScreen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.lab422.vkanalyzer.utils.command.Command

data class BarItem(
    @StringRes val toolbarTitle: Int?,
    @StringRes val itemTitle: Int,
    @DrawableRes val icon: Int,
    val id: Int,
    val command: Command,
    val fragmentTag: String
)
