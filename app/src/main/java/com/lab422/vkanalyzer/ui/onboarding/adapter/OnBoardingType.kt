package com.lab422.vkanalyzer.ui.onboarding.adapter

import com.lab422.vkanalyzer.ui.base.Diffable
import com.lab422.vkanalyzer.ui.base.Rawable

enum class OnBoardingType : Rawable, Diffable {
    OnBoarding;

    override val rawValue: Int = this.ordinal

    override fun isContentSame(same: Diffable): Boolean {
        if (same !is OnBoardingType) {
            return false
        }
        return same.rawValue == rawValue
    }

    override fun isSame(same: Diffable): Boolean {
        if (same !is OnBoardingType) {
            return false
        }

        return (same.rawValue == rawValue)
    }
}