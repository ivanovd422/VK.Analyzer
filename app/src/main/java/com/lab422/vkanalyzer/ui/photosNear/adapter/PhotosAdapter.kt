package com.lab422.vkanalyzer.ui.photosNear.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.lab422.vkanalyzer.databinding.ItemLoadingRowBinding
import com.lab422.vkanalyzer.databinding.ItemPhotosDateBinding
import com.lab422.vkanalyzer.databinding.ItemPhotosDateShimmerBinding
import com.lab422.vkanalyzer.databinding.ItemPhotosUserShimmerBinding
import com.lab422.vkanalyzer.databinding.ItemUserPhotoRowBinding
import com.lab422.vkanalyzer.ui.base.BaseViewHolder
import com.lab422.vkanalyzer.ui.photosNear.adapter.holder.DateViewHolder
import com.lab422.vkanalyzer.ui.photosNear.adapter.holder.LoadingViewHolder
import com.lab422.vkanalyzer.ui.photosNear.adapter.holder.PhotosViewHolder
import com.lab422.vkanalyzer.ui.photosNear.adapter.holder.ShimmerDateViewHolder
import com.lab422.vkanalyzer.ui.photosNear.adapter.holder.ShimmerPhotosViewHolder
import com.lab422.vkanalyzer.ui.photosNear.adapter.model.DatePhotosModel
import com.lab422.vkanalyzer.ui.photosNear.adapter.model.LoadingModel
import com.lab422.vkanalyzer.ui.photosNear.adapter.model.ShimmerDatePhotosModel
import com.lab422.vkanalyzer.ui.photosNear.adapter.model.ShimmerUserPhotoModel
import com.lab422.vkanalyzer.ui.photosNear.adapter.model.UserPhotoRowModel
import com.lab422.vkanalyzer.utils.imageLoader.ImageLoader

class PhotosAdapter(
    private val listener: PhotosViewHolder.Listener,
    private val onNextLoadingListener: LoadingViewHolder.Listener,
    private val imageLoader: ImageLoader
) : ListAdapter<Any, BaseViewHolder<*>>(PhotosAdapterDataDiff()) {

    private companion object {
        const val TYPE_USER_PHOTO = 1
        const val TYPE_DATE = 2
        const val TYPE_LOADING = 3
        const val TYPE_SHIMMER_DATE = 4
        const val TYPE_SHIMMER_PHOTO = 5
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is UserPhotoRowModel -> TYPE_USER_PHOTO
        is DatePhotosModel -> TYPE_DATE
        is LoadingModel -> TYPE_LOADING
        is ShimmerDatePhotosModel -> TYPE_SHIMMER_DATE
        is ShimmerUserPhotoModel -> TYPE_SHIMMER_PHOTO
        else -> throw IllegalArgumentException("Unsupported viewType: $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> =
        with(parent) {
            when (viewType) {
                TYPE_USER_PHOTO -> PhotosViewHolder(
                    ItemUserPhotoRowBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    listener,
                    imageLoader
                )
                TYPE_DATE -> DateViewHolder(
                    ItemPhotosDateBinding.inflate(
                        LayoutInflater.from(context), this, false
                    )
                )
                TYPE_LOADING -> LoadingViewHolder(
                    ItemLoadingRowBinding.inflate(
                        LayoutInflater.from(context), this, false
                    ),
                    onNextLoadingListener
                )

                TYPE_SHIMMER_DATE -> ShimmerDateViewHolder(
                    ItemPhotosDateShimmerBinding.inflate(
                        LayoutInflater.from(context), this, false
                    )
                )
                TYPE_SHIMMER_PHOTO -> ShimmerPhotosViewHolder(
                    ItemPhotosUserShimmerBinding.inflate(
                        LayoutInflater.from(context), this, false
                    )
                )
                else -> throw IllegalArgumentException("Unsupported viewType: $viewType")
            }
        }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        holder.bindHolder(getItem(position))
    }
}

private class PhotosAdapterDataDiff : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean =
        if (oldItem is UserPhotoRowModel && newItem is UserPhotoRowModel) {
            oldItem.userPhotosCells == newItem.userPhotosCells
        } else if (oldItem is DatePhotosModel && newItem is DatePhotosModel) {
            oldItem.date == newItem.date
        } else false

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean =
        if (oldItem is UserPhotoRowModel && newItem is UserPhotoRowModel) {
            oldItem.userPhotosCells == newItem.userPhotosCells
        } else if (oldItem is DatePhotosModel && newItem is DatePhotosModel) {
            oldItem.date == newItem.date
        } else false
}

