package com.lab422.vkanalyzer.ui.onboarding.adapter

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.lab422.vkanalyzer.ui.base.Diffable

data class OnBoardingViewData(
    @StringRes val title: Int,
    @StringRes val subTitle: Int,
    @DrawableRes val icon: Int
) : Diffable {
    override fun isSame(same: Diffable): Boolean {
        if (same !is OnBoardingViewData) {
            return false
        }
        return same.title == title
    }

    override fun isContentSame(same: Diffable): Boolean {
        if (same !is OnBoardingViewData) {
            return false
        }
        return same.title == title &&
            same.subTitle == subTitle &&
            same.icon == icon
    }
}
