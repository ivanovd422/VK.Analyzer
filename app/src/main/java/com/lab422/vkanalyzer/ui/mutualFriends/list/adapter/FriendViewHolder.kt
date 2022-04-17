package com.lab422.vkanalyzer.ui.mutualFriends.list.adapter

import android.net.Uri
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.databinding.ItemMutualFriendBinding
import com.lab422.vkanalyzer.ui.base.BaseViewHolder
import com.lab422.vkanalyzer.ui.mutualFriends.model.UserViewData
import com.lab422.vkanalyzer.utils.imageLoader.ImageLoader

class FriendViewHolder(
    private val binding: ItemMutualFriendBinding,
    private val listener: Listener?,
    private val imageLoader: ImageLoader
) : BaseViewHolder<UserViewData>(binding.root) {


    interface Listener {
        fun onFriendClicked(id: Long, name: String)
    }

    private companion object {
        const val textIsOnline = "Online"
        const val textIsOffline = "Offline"
        const val textIdPattern = "Id %s"

        @ColorRes
        const val colorGreenRes = R.color.colorGreenAlpha70

        @ColorRes
        const val colorRedRes = R.color.colorRedAlpha55
    }

    override fun onBind(model: UserViewData) {
        val textStatus = if (model.isOnline) textIsOnline else textIsOffline
        val statusColor = if (model.isOnline) colorGreenRes else colorRedRes

        binding.root.setOnClickListener { listener?.onFriendClicked(model.id, model.userName) }

        with(binding) {
            tvUserName.text = model.userName
            tvUserStatus.text = textStatus
            tvUserStatus.setTextColor(ContextCompat.getColor(itemView.context, statusColor))
            tvUserId.text = String.format(textIdPattern, model.id)
        }

        model.photoUrl?.let { photoUrl ->
            imageLoader.loadPhotoByUrl(
                binding.ivFriendPhoto,
                photoUrl,
                RequestOptions.circleCropTransform()
            )
        }
    }
}
