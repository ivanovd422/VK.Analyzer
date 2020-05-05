package com.lab422.vkanalyzer.ui.mutualFriends.list.adapter

import androidx.lifecycle.LifecycleOwner
import com.lab422.vkanalyzer.ui.base.BaseTypedAdapter
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.common.StringProvider

class MutualFriendsListAdapter(
    friends: List<RowDataModel<FriendsListType, *>>,
    stringProvider: com.lab422.common.StringProvider,
    lifecycleOwner: LifecycleOwner,
    onFriendClicked: FriendViewHolder.Listener
) : BaseTypedAdapter<FriendsListType>(friends, stringProvider, true, lifecycleOwner) {

    init {
        addFactory(FriendsListType.Friends, FriendViewHolder.getFactory(onFriendClicked))
    }
}