package com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider

import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.ui.mutualFriends.model.UserViewModel
import com.lab422.vkanalyzer.utils.vkModels.user.User

class FriendsDataProviderImpl : FriendsListDataProvider {

    override fun generateFriendsListData(friends: List<User>, type: FriendsListType): List<RowDataModel<FriendsListType, *>> {
        val friendsList = mutableListOf<RowDataModel<FriendsListType, *>>()

        friends.forEach {
            friendsList.add(
                RowDataModel(
                    type,
                    it.id.toString(),
                    UserViewModel(
                        it.id,
                        "${it.first_name}  ${it.last_name}",
                        it.online != 0,
                        it.photo_200
                    )
                )
            )
        }

        return friendsList
    }
}