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

class LoadingViewHolder(
    view: View,
    private val listener: Listener?
) : BaseTypedViewHolder<UserPhotoRowType>(view) {

    interface Listener {
        fun onNextLoading()
    }

    companion object {
        private class Factory(private var listener: Listener?) : ViewHolderFactory {
            @Suppress("unused")
            override fun <T> createViewHolder(
                parent: ViewGroup,
                viewType: Int,
                stringProvider: StringProvider,
                imageLoader: ImageLoader
            ): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading_row, parent, false)
                return LoadingViewHolder(
                    view,
                    listener
                )
            }
        }

        fun getFactory(listener: Listener? = null): ViewHolderFactory {
            return Factory(
                listener
            )
        }
    }

    override fun onBind(model: RowDataModel<UserPhotoRowType, *>) {
        super.onBind(model)
        listener?.onNextLoading()
    }
}
