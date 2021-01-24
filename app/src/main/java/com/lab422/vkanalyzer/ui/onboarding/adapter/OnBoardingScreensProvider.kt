package com.lab422.vkanalyzer.ui.onboarding.adapter

import com.lab422.vkanalyzer.R

object OnBoardingScreensProvider {

    private val onBoardingData = listOf(
        OnBoardingViewData(
            R.string.on_boarding_title_1,
            R.string.on_boarding_subtitle_1,
            R.drawable.ic_onboarding_screen_1
        ),
        OnBoardingViewData(
            R.string.on_boarding_title_2,
            R.string.on_boarding_subtitle_2,
            R.drawable.ic_onboarding_screen_2
        ),
        OnBoardingViewData(
            R.string.on_boarding_title_3,
            R.string.on_boarding_subtitle_3,
            R.drawable.ic_onboarding_screen_3
        )
    )

    fun getOnBoardingScreenViewData(): List<OnBoardingViewData> =
        onBoardingData

    fun getScreensCount(): Int = onBoardingData.size
}
