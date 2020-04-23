package com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider

import com.lab422.analyzerapi.models.users.NewUser
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.ui.mutualFriends.model.UserViewModel
import com.lab422.vkanalyzer.utils.vkModels.user.User
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
            if (it.first_name.toLowerCase().contains(query) || it.last_name.toLowerCase().contains(query)) {
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
                "${user.first_name}  ${user.last_name}",
                user.online != 0,
                user.photoUrl
            )
        )


    override fun generateFriendsListDataOld(
        friends: List<User>,
        type: FriendsListType
    ): List<RowDataModel<FriendsListType, *>> {
        val friendsList = mutableListOf<RowDataModel<FriendsListType, *>>()

        friends.forEach {
            friendsList.add(
                createModelOld(it, type)
            )
        }

        return friendsList
    }

    override suspend fun filterByQueryOld(
        friends: List<User>,
        type: FriendsListType,
        query: String
    ): List<RowDataModel<FriendsListType, *>> = withContext(Dispatchers.Default) {
        val filteredData = mutableListOf<RowDataModel<FriendsListType, *>>()

        friends.forEach {
            if (it.first_name.toLowerCase().contains(query) || it.last_name.toLowerCase().contains(query)) {
                filteredData.add(
                    createModelOld(it, type)
                )
            }
        }

        filteredData
    }

    private fun createModelOld(user: User, type: FriendsListType): RowDataModel<FriendsListType, *> =
        RowDataModel(
            type,
            user.id.toString(),
            UserViewModel(
                user.id,
                "${user.first_name}  ${user.last_name}",
                user.online != 0,
                user.photoUrl
            )
        )
}