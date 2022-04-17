package com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider

import com.lab422.analyzerapi.models.users.NewUser
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.ui.mutualFriends.model.UserViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FriendsDataProviderImpl : FriendsListDataProvider {

    override fun generateFriendsListData(
        friends: List<NewUser>,
        type: FriendsListType
    ): List<UserViewData> {
        val friendsList = mutableListOf<UserViewData>()

        friends.forEach {
            friendsList.add(
                createModel(it)
            )
        }

        return friendsList
    }

    override suspend fun filterByQuery(
        friends: List<NewUser>,
        type: FriendsListType,
        query: String
    ): List<UserViewData> = withContext(Dispatchers.Default) {
        val filteredData = mutableListOf<UserViewData>()

        friends.forEach {
            if (it.firstName.toLowerCase().contains(query) || it.lastName.toLowerCase().contains(query)) {
                filteredData.add(
                    createModel(it)
                )
            }
        }

        filteredData
    }

    private fun createModel(user: NewUser): UserViewData =
        UserViewData(
            user.id,
            "${user.firstName}  ${user.lastName}",
            user.online != 0,
            user.photoUrl
        )
}
