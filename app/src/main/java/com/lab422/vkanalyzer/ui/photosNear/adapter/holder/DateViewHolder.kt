package com.lab422.vkanalyzer.ui.photosNear.adapter.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lab422.common.StringProvider
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.base.BaseTypedViewHolder
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.base.ViewHolderFactory
import com.lab422.vkanalyzer.ui.photosNear.adapter.UserPhotoRowType
import com.lab422.vkanalyzer.utils.imageLoader.ImageLoader
import kotlinx.android.synthetic.main.item_photos_date.view.*

class DateViewHolder(
    view: View
) : BaseTypedViewHolder<UserPhotoRowType>(view) {

    private val tvDate = view.tv_date

    companion object {
        private class Factory : ViewHolderFactory {
            @Suppress("unused")
            override fun <T> createViewHolder(
                parent: ViewGroup,
                viewType: Int,
                stringProvider: StringProvider,
                imageLoader: ImageLoader
            ): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photos_date, parent, false)
                return DateViewHolder(view)
            }
        }

        fun getFactory(): ViewHolderFactory = Factory()
    }

    override fun onBind(model: RowDataModel<UserPhotoRowType, *>) {
        super.onBind(model)
        val item = model.value as String
        tvDate.text = item
    }
}
