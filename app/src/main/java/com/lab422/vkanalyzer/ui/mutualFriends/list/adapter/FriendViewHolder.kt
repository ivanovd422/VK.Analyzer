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

class FriendViewHolder(
    private val view: View,
    private val listener: Listener?
) : BaseTypedViewHolder<FriendsListType>(view) {
    private val tvUserName = view.tv_user_name
    private val tvUserStatus = view.tv_user_status

    interface Listener {
        fun onFriendClicked(id: Long)
    }

    companion object {
        private class Factory(private var listener: Listener?) : ViewHolderFactory {
            @Suppress("unused")
            override fun <T> createViewHolder(
                parent: ViewGroup,
                viewType: Int,
                stringProvider: StringProvider
            ): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mutual_friend, parent, false)
                return FriendViewHolder(view, listener)
            }
        }

        fun getFactory(listener: Listener? = null): ViewHolderFactory {
            return Factory(listener)
        }
    }

    override fun onBind(model: RowDataModel<FriendsListType, *>) {
        super.onBind(model)
        val item = model.value as UserViewModel
        val isClickable = model.rowType == FriendsListType.SelectableFriends
        val status = if (item.isOnline) "Status: Online" else "Status: Offline"

        tvUserName.text = item.userName
        tvUserStatus.text = status

        view.isFocusable = isClickable
        view.isClickable = isClickable

        if (isClickable) {
            view.setOnClickListener { listener?.onFriendClicked(item.id) }
        }
    }
}

