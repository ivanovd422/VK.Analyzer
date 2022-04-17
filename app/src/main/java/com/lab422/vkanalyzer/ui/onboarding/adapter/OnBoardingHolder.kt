package com.lab422.vkanalyzer.ui.onboarding.adapter

import com.lab422.vkanalyzer.databinding.ItemOnboardingHolderBinding
import com.lab422.vkanalyzer.ui.base.BaseViewHolder

class OnBoardingHolder(
    private val binding: ItemOnboardingHolderBinding
) : BaseViewHolder<OnBoardingViewData>(binding.root) {

    override fun onBind(model: OnBoardingViewData) {
        with(binding) {
            tvOnBoardingTitle.setText(model.title)
            tvOnBoardingSubtitle.setText(model.subTitle)
            ivOnBoardingImage.setImageResource(model.icon)
        }
    }
}

