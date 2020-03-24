package com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider

import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.utils.vkModels.user.User

interface FriendsListDataProvider {
    fun generateFriendsListData(friends: List<User>, type: FriendsListType): List<RowDataModel<FriendsListType, *>>
    suspend fun filterByQuery(friends: List<User>, type: FriendsListType, query: String): List<RowDataModel<FriendsListType, *>>
}