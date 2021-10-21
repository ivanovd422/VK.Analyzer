package com.lab422.vkanalyzer.ui.onboarding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lab422.common.StringProvider
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.databinding.ItemOnboardingHolderBinding
import com.lab422.vkanalyzer.ui.base.BaseTypedViewHolder
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.base.ViewHolderFactory
import com.lab422.vkanalyzer.utils.imageLoader.ImageLoader

class OnBoardingHolder(
    view: View
) : BaseTypedViewHolder<OnBoardingType>(view) {

    private val binding = ItemOnboardingHolderBinding.bind(itemView)

    companion object {
        private class Factory : ViewHolderFactory {
            @Suppress("unused")
            override fun <T> createViewHolder(
                parent: ViewGroup,
                viewType: Int,
                stringProvider: StringProvider,
                imageLoader: ImageLoader
            ): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_onboarding_holder, parent, false)
                return OnBoardingHolder(view)
            }
        }

        fun getFactory(): ViewHolderFactory = Factory()
    }

    override fun onBind(model: RowDataModel<OnBoardingType, *>) {
        super.onBind(model)
        with(model.value as OnBoardingViewData) {
            with(binding) {
                tvOnBoardingTitle.setText(title)
                tvOnBoardingSubtitle.setText(subTitle)
                ivOnBoardingImage.setImageResource(icon)
            }
        }
    }
}

