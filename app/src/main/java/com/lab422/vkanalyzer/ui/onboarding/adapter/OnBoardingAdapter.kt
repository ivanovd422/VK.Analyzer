package com.lab422.vkanalyzer.ui.onboarding.adapter

import androidx.lifecycle.LifecycleOwner
import com.lab422.vkanalyzer.ui.base.BaseTypedAdapter
import com.lab422.vkanalyzer.ui.base.RowDataModel

class OnBoardingAdapter(
    friends: List<RowDataModel<OnBoardingType, *>>,
    stringProvider: com.lab422.common.StringProvider,
    lifecycleOwner: LifecycleOwner
) : BaseTypedAdapter<OnBoardingType>(friends, stringProvider, true, lifecycleOwner) {

    init {
        addFactory(OnBoardingType.OnBoarding, OnBoardingHolder.getFactory())
    }
}