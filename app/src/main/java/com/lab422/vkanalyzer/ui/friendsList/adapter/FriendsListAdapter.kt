package com.lab422.vkanalyzer.ui.friendsList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendViewHolder
import com.lab422.vkanalyzer.ui.mutualFriends.model.UserViewData
import androidx.recyclerview.widget.ListAdapter
import com.lab422.vkanalyzer.databinding.ItemMutualFriendBinding
import com.lab422.vkanalyzer.utils.imageLoader.ImageLoader

class FriendsListAdapter(
    private val listener: FriendViewHolder.Listener,
    private val imageLoader: ImageLoader
) : ListAdapter<UserViewData, FriendViewHolder>(UserViewDataDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder =
        FriendViewHolder(
            ItemMutualFriendBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            listener,
            imageLoader
        )

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bindHolder(getItem(position))
    }
}

private class UserViewDataDiff : DiffUtil.ItemCallback<UserViewData>() {

    override fun areItemsTheSame(oldItem: UserViewData, newItem: UserViewData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserViewData, newItem: UserViewData): Boolean {
        return oldItem == newItem
    }
}
