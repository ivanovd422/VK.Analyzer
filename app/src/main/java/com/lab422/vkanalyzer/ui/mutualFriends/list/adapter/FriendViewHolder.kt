package com.lab422.vkanalyzer.ui.mutualFriends.list.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.lab422.common.StringProvider
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.databinding.ItemMutualFriendBinding
import com.lab422.vkanalyzer.ui.base.BaseTypedViewHolder
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.base.ViewHolderFactory
import com.lab422.vkanalyzer.ui.mutualFriends.model.UserViewModel
import com.lab422.vkanalyzer.utils.imageLoader.ImageLoader

class FriendViewHolder(
    private val view: View,
    private val listener: Listener?
) : BaseTypedViewHolder<FriendsListType>(view) {

    private val binding = ItemMutualFriendBinding.bind(itemView)

    interface Listener {
        fun onFriendClicked(id: Long, name: String)
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
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mutual_friend, parent, false)
                return FriendViewHolder(view, listener)
            }
        }

        fun getFactory(listener: Listener? = null): ViewHolderFactory {
            return Factory(listener)
        }

        private const val textIsOnline = "Online"
        private const val textIsOffline = "Offline"
        private const val textIdPattern = "Id %s"

        @ColorRes
        private const val colorGreenRes = R.color.colorGreenAlpha70

        @ColorRes
        private const val colorRedRes = R.color.colorRedAlpha55
    }

    override fun onBind(model: RowDataModel<FriendsListType, *>) {
        super.onBind(model)
        val item = model.value as UserViewModel
        val textStatus = if (item.isOnline) textIsOnline else textIsOffline
        val statusColor = if (item.isOnline) colorGreenRes else colorRedRes

        view.setOnClickListener { listener?.onFriendClicked(item.id, item.userName) }

        with(binding) {
            tvUserName.text = item.userName
            tvUserStatus.text = textStatus
            tvUserStatus.setTextColor(ContextCompat.getColor(itemView.context, statusColor))
            tvUserId.text = String.format(textIdPattern, item.id)
        }

        if (item.photoUrl.isNullOrEmpty().not()) {
            Glide.with(itemView.context)
                .asBitmap()
                .load(Uri.parse(item.photoUrl))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivFriendPhoto)
        }
    }
}
