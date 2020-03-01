package com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider

import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.MutualFriendsType
import com.lab422.vkanalyzer.ui.mutualFriends.model.UserViewModel
import com.lab422.vkanalyzer.utils.vkModels.user.User

class MutualFriendsDataProviderImpl : MutualFriendsDataProvider {

    override fun generateMutualFriendsData(friends: List<User>): List<RowDataModel<MutualFriendsType, *>> {
        val mutualFriends = mutableListOf<RowDataModel<MutualFriendsType, *>>()

        friends.forEach {
            mutualFriends.add(
                RowDataModel(
                    MutualFriendsType.Friends,
                    it.id.toString(),
                    UserViewModel(
                        it.id,
                        "${it.first_name}  ${it.last_name}",
                        it.online != 0
                    )
                )
            )
        }

        return mutualFriends
    }
}