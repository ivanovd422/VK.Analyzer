package com.lab422.vkanalyzer.ui.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.lab422.vkanalyzer.databinding.ItemOnboardingHolderBinding

class OnBoardingAdapter : ListAdapter<OnBoardingViewData, OnBoardingHolder>(OnBoardingDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingHolder =
        OnBoardingHolder(
            ItemOnboardingHolderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: OnBoardingHolder, position: Int) {
        holder.bindHolder(getItem(position))
    }
}

private class OnBoardingDiff : DiffUtil.ItemCallback<OnBoardingViewData>() {

    override fun areItemsTheSame(oldItem: OnBoardingViewData, newItem: OnBoardingViewData): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: OnBoardingViewData, newItem: OnBoardingViewData): Boolean {
        return oldItem == newItem
    }
}
