package com.lab422.vkanalyzer.ui.onboarding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lab422.common.StringProvider
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.base.BaseTypedViewHolder
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.base.ViewHolderFactory
import kotlinx.android.synthetic.main.item_onboarding_holder.view.*

class OnBoardingHolder(
    view: View
) : BaseTypedViewHolder<OnBoardingType>(view) {

    private val tvTitle = view.tv_on_boarding_title
    private val tvSubTitle = view.tv_on_boarding_subtitle
    private val ivImage = view.iv_on_boarding_image

    companion object {
        private class Factory : ViewHolderFactory {
            @Suppress("unused")
            override fun <T> createViewHolder(
                parent: ViewGroup,
                viewType: Int,
                stringProvider: StringProvider
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
            tvTitle.setText(title)
            tvSubTitle.setText(subTitle)
            ivImage.setImageResource(icon)
        }
    }
}

