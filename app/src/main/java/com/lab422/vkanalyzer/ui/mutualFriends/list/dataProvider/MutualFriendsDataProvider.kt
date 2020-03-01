package com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider

import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.MutualFriendsType
import com.lab422.vkanalyzer.utils.vkModels.user.User

interface MutualFriendsDataProvider {
    fun generateMutualFriendsData(friends: List<User>): List<RowDataModel<MutualFriendsType, *>>
}