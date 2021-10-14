package com.lab422.interactor

import com.lab422.analyzerapi.NetworkApiException
import com.lab422.analyzerapi.NetworkResponse
import com.lab422.analyzerapi.UsersApi
import com.lab422.analyzerapi.isFailure
import com.lab422.analyzerapi.map
import com.lab422.analyzerapi.models.friendsList.convertToUser
import com.lab422.analyzerapi.models.users.NewUser
import com.lab422.analyzerapi.value
import com.lab422.common.UserNameValidator

class UserInteractor constructor(
    private val usersApi: UsersApi,
    private val validator: UserNameValidator
) {
    suspend fun getFriendsList(): NetworkResponse<List<NewUser>> =
        usersApi.getFriendsList().map { response -> response.response.items.map { item -> item.convertToUser() } }

    suspend fun findMutualFriendsByUsersId(
        firstName: String,
        secondName: String
    ): NetworkResponse<List<NewUser>> {
        var firstUserId: String = validator.validate(firstName)
        var secondUserId: String = validator.validate(secondName)
        val isKnowFirstUserId: Boolean = validator.isId(firstUserId)
        val isKnowSecondUserId: Boolean = validator.isId(secondUserId)

        if (isKnowFirstUserId.not() || isKnowSecondUserId.not()) {
            val nicknames = mutableListOf<String>()

            if (isKnowFirstUserId.not()) {
                nicknames.add(firstUserId)
            }

            if (isKnowSecondUserId.not()) {
                nicknames.add(secondUserId)
            }

            val idString = nicknames.joinToString(separator = ",")
            val usersIdResponse = usersApi.getUsersByIds(idString).map { it.response.map { it.id.toString() } }

            if (usersIdResponse.isFailure() || usersIdResponse.value().isNullOrEmpty()) {
                return NetworkResponse.failure(NetworkApiException("Проверьте правильность полей", null, null), null)
            }
            val usersId = usersIdResponse.value()!!

            firstUserId = if (isKnowFirstUserId) firstUserId else usersId.first()
            secondUserId = if (isKnowSecondUserId) secondUserId else {
                if (usersId.size == 1) {
                    usersId.first()
                } else {
                    usersId.last()
                }
            }
        }

        val mutualFriendsResponse = usersApi.getMutualFriendsId(firstUserId, secondUserId)

        if (mutualFriendsResponse.isFailure() || mutualFriendsResponse.value()?.response.isNullOrEmpty()) {
            return NetworkResponse.failure(NetworkApiException("Проверьте правильность полей", null, null), null)
        }

        val usersId = mutualFriendsResponse.map { it.response.joinToString(",") }

        return usersApi.getUsersWithInfoByIds(usersId.value()!!).map { it.response }
    }

    suspend fun getUserInfoById(userId: String): NetworkResponse<NewUser?> =
        usersApi.getUsersWithInfoByIds(userId).map { it.response.firstOrNull() }
}
