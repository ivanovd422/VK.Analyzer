package com.lab422.vkanalyzer.ui.photosNear.adapter

import androidx.lifecycle.LifecycleOwner
import com.lab422.common.StringProvider
import com.lab422.vkanalyzer.ui.base.BaseTypedAdapter
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.photosNear.adapter.holder.DateViewHolder
import com.lab422.vkanalyzer.ui.photosNear.adapter.holder.LoadingViewHolder
import com.lab422.vkanalyzer.ui.photosNear.adapter.holder.PhotosViewHolder
import com.lab422.vkanalyzer.utils.imageLoader.ImageLoader

class PhotosAdapter(
    friends: List<RowDataModel<UserPhotoRowType, *>>,
    stringProvider: StringProvider,
    lifecycleOwner: LifecycleOwner,
    listener: PhotosViewHolder.Listener,
    onNextLoadingListener: LoadingViewHolder.Listener,
    imageLoader: ImageLoader
) : BaseTypedAdapter<UserPhotoRowType>(friends, stringProvider, true, lifecycleOwner, imageLoader) {

    init {
        addFactory(UserPhotoRowType.UserPhoto, PhotosViewHolder.getFactory(listener))
        addFactory(UserPhotoRowType.Loading, LoadingViewHolder.getFactory(onNextLoadingListener))
        addFactory(UserPhotoRowType.Date, DateViewHolder.getFactory())
    }
}
