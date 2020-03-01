package com.lab422.vkanalyzer.ui.mutualFriends.list.adapter

import com.lab422.vkanalyzer.ui.base.BaseTypedAdapter
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.utils.stringProvider.StringProvider

class MutualFriendsAdapter(
    mutualFriends: List<RowDataModel<MutualFriendsType, *>>,
    stringProvider: StringProvider
) : BaseTypedAdapter<MutualFriendsType>(mutualFriends, stringProvider) {

    init {
        addFactory(MutualFriendsType.Friends, FriendViewHolder)
    }
}