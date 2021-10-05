package com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider

import com.lab422.analyzerapi.models.users.NewUser
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.ui.mutualFriends.model.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FriendsDataProviderImpl : FriendsListDataProvider {

    override fun generateFriendsListData(
        friends: List<NewUser>,
        type: FriendsListType
    ): List<RowDataModel<FriendsListType, *>> {
        val friendsList = mutableListOf<RowDataModel<FriendsListType, *>>()

        friends.forEach {
            friendsList.add(
                createModel(it, type)
            )
        }

        return friendsList
    }

    override suspend fun filterByQuery(
        friends: List<NewUser>,
        type: FriendsListType,
        query: String
    ): List<RowDataModel<FriendsListType, *>> = withContext(Dispatchers.Default) {
        val filteredData = mutableListOf<RowDataModel<FriendsListType, *>>()

        friends.forEach {
            if (it.firstName.toLowerCase().contains(query) || it.lastName.toLowerCase().contains(query)) {
                filteredData.add(
                    createModel(it, type)
                )
            }
        }

        filteredData
    }

    private fun createModel(user: NewUser, type: FriendsListType): RowDataModel<FriendsListType, *> =
        RowDataModel(
            type,
            user.id.toString(),
            UserViewModel(
                user.id,
                "${user.firstName}  ${user.lastName}",
                user.online != 0,
                user.photoUrl
            )
        )
}
