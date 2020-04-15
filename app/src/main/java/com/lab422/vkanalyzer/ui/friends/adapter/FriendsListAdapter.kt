package com.lab422.vkanalyzer.ui.friends.adapter

import androidx.lifecycle.LifecycleOwner
import com.lab422.vkanalyzer.ui.base.BaseTypedAdapter
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendViewHolder
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.common.StringProvider

class FriendsListAdapter(
    friends: List<RowDataModel<FriendsListType, *>>,
    stringProvider: com.lab422.common.StringProvider,
    lifecycleOwner: LifecycleOwner,
    listener: FriendViewHolder.Listener
) : BaseTypedAdapter<FriendsListType>(friends, stringProvider, true, lifecycleOwner) {

    init {
        addFactory(FriendsListType.SelectableFriends, FriendViewHolder.getFactory(listener))
    }
}