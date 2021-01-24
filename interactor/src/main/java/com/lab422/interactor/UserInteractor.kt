package com.lab422.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lab422.analyzerapi.UsersApi
import com.lab422.analyzerapi.models.friendsList.convertToUser
import com.lab422.analyzerapi.models.users.NewUser
import com.lab422.common.UserNameValidator
import com.lab422.common.viewState.ViewState

class UserInteractor constructor(
    private val usersApi: UsersApi,
    private val validator: UserNameValidator
) : BaseInteractor() {
    suspend fun getFriendsList(): LiveData<ViewState<List<NewUser>>> = invokeBlock {
        val liveData = MutableLiveData<ViewState<List<NewUser>>>()
        val friendsList = mutableListOf<NewUser>()
        val result = usersApi.getFriendsList()
        result.response.items.forEach { friendsList.add(it.convertToUser()) }
        liveData.postValue(ViewState(ViewState.Status.SUCCESS, friendsList))

        return@invokeBlock liveData
    }

    suspend fun findMutualFriendsByUsersId(
        firstName: String,
        secondName: String
    ): LiveData<ViewState<List<NewUser>>> = invokeBlock {
        val liveData = MutableLiveData<ViewState<List<NewUser>>>()
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
            val usersId = usersApi.getUsersByIds(idString).response.map { it.id.toString() }

            // todo throw app exception
            if (usersId.isEmpty()) {
                liveData.postValue(ViewState(ViewState.Status.ERROR, error = "Проверьте правильность полей"))
                return@invokeBlock liveData
            }

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
        val usersId = mutualFriendsResponse.response.joinToString(",")
        val usersList = usersApi.getUsersWithInfoByIds(usersId).response

        liveData.postValue(ViewState(ViewState.Status.SUCCESS, usersList))
        return@invokeBlock liveData
    }

    suspend fun getUserInfoById(userId: String): LiveData<ViewState<NewUser>> = invokeBlock {
        val liveData = MutableLiveData<ViewState<NewUser>>()
        val newUser = usersApi.getUsersWithInfoByIds(userId).response.first()
        liveData.postValue(ViewState(ViewState.Status.SUCCESS, newUser))

        return@invokeBlock liveData
    }
}
