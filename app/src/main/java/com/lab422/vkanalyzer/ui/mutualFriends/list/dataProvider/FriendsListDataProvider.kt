package com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider

import com.lab422.analyzerapi.models.users.NewUser
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.ui.mutualFriends.model.UserViewData

interface FriendsListDataProvider {

    fun generateFriendsListData(friends: List<NewUser>, type: FriendsListType): List<UserViewData>
    suspend fun filterByQuery(
        friends: List<NewUser>,
        type: FriendsListType,
        query: String
    ): List<UserViewData>
}
