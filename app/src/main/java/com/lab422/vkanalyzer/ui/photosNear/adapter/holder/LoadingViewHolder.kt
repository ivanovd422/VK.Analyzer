package com.lab422.vkanalyzer.ui.photosNear.adapter.holder

import com.lab422.vkanalyzer.databinding.ItemLoadingRowBinding
import com.lab422.vkanalyzer.ui.base.BaseViewHolder
import com.lab422.vkanalyzer.ui.photosNear.adapter.model.LoadingModel

class LoadingViewHolder(
    binding: ItemLoadingRowBinding,
    private val listener: Listener?
) : BaseViewHolder<LoadingModel>(binding.root) {

    interface Listener {
        fun onNextLoading()
    }

    override fun onBind(model: LoadingModel) {
        listener?.onNextLoading()
    }
}
