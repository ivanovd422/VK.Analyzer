package com.lab422.vkanalyzer.ui.mutualFriends.list.adapter

import androidx.lifecycle.LifecycleOwner
import com.lab422.common.StringProvider
import com.lab422.vkanalyzer.ui.base.BaseTypedAdapter
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.utils.imageLoader.ImageLoader

class MutualFriendsListAdapter(
    friends: List<RowDataModel<FriendsListType, *>>,
    stringProvider: com.lab422.common.StringProvider,
    lifecycleOwner: LifecycleOwner,
    onFriendClicked: FriendViewHolder.Listener,
    imageLoader: ImageLoader
) : BaseTypedAdapter<FriendsListType>(friends, stringProvider, true, lifecycleOwner, imageLoader) {

    init {
        addFactory(FriendsListType.Friends, FriendViewHolder.getFactory(onFriendClicked))
    }
}
