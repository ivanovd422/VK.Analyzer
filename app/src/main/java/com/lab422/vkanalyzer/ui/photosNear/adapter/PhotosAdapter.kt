package com.lab422.vkanalyzer.ui.photosNear.adapter

import androidx.lifecycle.LifecycleOwner
import com.lab422.common.StringProvider
import com.lab422.vkanalyzer.ui.base.BaseTypedAdapter
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.photosNear.adapter.holder.DateViewHolder
import com.lab422.vkanalyzer.ui.photosNear.adapter.holder.LoadingViewHolder
import com.lab422.vkanalyzer.ui.photosNear.adapter.holder.PhotosViewHolder

class PhotosAdapter(
    friends: List<RowDataModel<UserPhotoRowType, *>>,
    stringProvider: StringProvider,
    lifecycleOwner: LifecycleOwner,
    listener: PhotosViewHolder.Listener,
    onNextLoadingListener: LoadingViewHolder.Listener
) : BaseTypedAdapter<UserPhotoRowType>(friends, stringProvider, true, lifecycleOwner) {

    init {
        addFactory(UserPhotoRowType.UserPhotoRowType, PhotosViewHolder.getFactory(listener))
        addFactory(UserPhotoRowType.Loading, LoadingViewHolder.getFactory(onNextLoadingListener))
        addFactory(UserPhotoRowType.Date, DateViewHolder.getFactory())
    }
}