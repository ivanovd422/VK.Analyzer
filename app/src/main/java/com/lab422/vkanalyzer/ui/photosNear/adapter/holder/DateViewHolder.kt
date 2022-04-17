package com.lab422.vkanalyzer.ui.photosNear.adapter.holder

import com.lab422.vkanalyzer.databinding.ItemPhotosDateBinding
import com.lab422.vkanalyzer.ui.base.BaseViewHolder
import com.lab422.vkanalyzer.ui.photosNear.adapter.model.DatePhotosModel

class DateViewHolder(
    private val binding: ItemPhotosDateBinding
) : BaseViewHolder<DatePhotosModel>(binding.root) {

    override fun onBind(model: DatePhotosModel) {
        binding.tvDate.text = model.date
    }
}
