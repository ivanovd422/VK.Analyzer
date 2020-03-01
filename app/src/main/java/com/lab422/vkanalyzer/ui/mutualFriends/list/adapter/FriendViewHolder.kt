package com.lab422.vkanalyzer.ui.mutualFriends.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.base.BaseTypedViewHolder
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.base.ViewHolderFactory
import com.lab422.vkanalyzer.ui.mutualFriends.model.UserViewModel
import com.lab422.vkanalyzer.utils.stringProvider.StringProvider
import kotlinx.android.synthetic.main.item_mutual_friend.view.*

class FriendViewHolder(view: View) : BaseTypedViewHolder<MutualFriendsType>(view) {
    private val tvUserName = view.tv_user_name
    private val tvUserStatus = view.tv_user_status

    companion object : ViewHolderFactory {
        @Suppress("unused")
        override fun <T> createViewHolder(
            parent: ViewGroup,
            viewType: Int,
            stringProvider: StringProvider
        ): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mutual_friend, parent, false)
            return FriendViewHolder(view)
        }
    }

    override fun onBind(model: RowDataModel<MutualFriendsType, *>) {
        super.onBind(model)
        val item = model.value as UserViewModel
        tvUserName.text = item.userName
        val status = if (item.isOnline) "Status: Online" else "Status: Offline"
        tvUserStatus.text = status
    }
}

