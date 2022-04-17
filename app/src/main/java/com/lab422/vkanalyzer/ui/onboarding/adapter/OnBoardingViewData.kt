package com.lab422.vkanalyzer.ui.onboarding.adapter

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnBoardingViewData(
    @StringRes val title: Int,
    @StringRes val subTitle: Int,
    @DrawableRes val icon: Int
)
