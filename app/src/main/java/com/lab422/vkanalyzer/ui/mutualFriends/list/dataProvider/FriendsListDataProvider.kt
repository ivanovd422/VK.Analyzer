package com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider

import com.lab422.analyzerapi.models.users.NewUser
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType

interface FriendsListDataProvider {
    fun generateFriendsListData(friends: List<NewUser>, type: FriendsListType): List<RowDataModel<FriendsListType, *>>
    suspend fun filterByQuery(friends: List<NewUser>, type: FriendsListType, query: String): List<RowDataModel<FriendsListType, *>>
}