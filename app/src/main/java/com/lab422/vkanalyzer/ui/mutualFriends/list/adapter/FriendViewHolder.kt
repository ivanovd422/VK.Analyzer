package com.lab422.vkanalyzer.ui.mutualFriends.list.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lab422.common.StringProvider
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.base.BaseTypedViewHolder
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.base.ViewHolderFactory
import com.lab422.vkanalyzer.ui.mutualFriends.model.UserViewModel
import com.lab422.vkanalyzer.utils.extensions.setVisible
import kotlinx.android.synthetic.main.item_mutual_friend.view.*

class FriendViewHolder(
    private val view: View,
    private val listener: Listener?
) : BaseTypedViewHolder<FriendsListType>(view) {
    private val tvUserName = view.tv_user_name
    private val tvUserStatus = view.tv_user_status
    private val ivUserPhoto = view.iv_friend_photo
    private val tvUserId = view.tv_user_id
    private val ivSearch = view.iv_search

    interface Listener {
        fun onFriendClicked(id: Long, name: String)
    }

    companion object {
        private class Factory(private var listener: Listener?) : ViewHolderFactory {
            @Suppress("unused")
            override fun <T> createViewHolder(
                parent: ViewGroup,
                viewType: Int,
                stringProvider: com.lab422.common.StringProvider
            ): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mutual_friend, parent, false)
                return FriendViewHolder(view, listener)
            }
        }

        fun getFactory(listener: Listener? = null): ViewHolderFactory {
            return Factory(listener)
        }

        private const val onlineStatusPattern = "Status: %s"
        private const val textIsOnline = "Online"
        private const val textIsOffline = "Offline"
        private const val textIdPattern = "Id: %s"

        @ColorRes
        private const val colorGreenRes = R.color.colorGreen
        @ColorRes
        private const val colorRedRes = R.color.colorRed
    }

    override fun onBind(model: RowDataModel<FriendsListType, *>) {
        super.onBind(model)
        val item = model.value as UserViewModel
        val isClickable = model.rowType == FriendsListType.Friends

        val status = if (item.isOnline) textIsOnline else textIsOffline
        val statusColor = if (item.isOnline) colorGreenRes else colorRedRes
        val textStatus = String.format(onlineStatusPattern, status)

        view.setOnClickListener { listener?.onFriendClicked(item.id, item.userName) }

        ivSearch.setVisible(isClickable)

        tvUserName.text = item.userName

        tvUserStatus.text = textStatus
        tvUserStatus.setTextColor(ContextCompat.getColor(itemView.context, statusColor))

        tvUserId.text = String.format(textIdPattern, item.id)

        if (item.photoUrl.isNullOrEmpty().not()) {
            Glide.with(itemView.context)
                .asBitmap()
                .load(Uri.parse(item.photoUrl))
                .apply(RequestOptions.circleCropTransform())
                .into(ivUserPhoto)
        }
    }
}
