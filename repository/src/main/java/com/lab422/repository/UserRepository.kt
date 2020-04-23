package com.lab422.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lab422.analyzerapi.UsersApi
import com.lab422.analyzerapi.models.friendsList.convertToUser
import com.lab422.analyzerapi.models.users.NewUser
import com.lab422.common.UserNameValidator
import com.lab422.common.viewState.ViewState

class UserRepository constructor(
    private val usersApi: UsersApi,
    private val validator: UserNameValidator
) {
    suspend fun getFriendsList(): LiveData<ViewState<List<NewUser>>> {
        val liveData = MutableLiveData<ViewState<List<NewUser>>>()
        try {
            val friendsList = mutableListOf<NewUser>()
            val result = usersApi.getFriendsList()
            result.response.items.forEach { friendsList.add(it.convertToUser()) }
            liveData.postValue(ViewState(ViewState.Status.SUCCESS, friendsList))
        } catch (e: Exception) {
            liveData.postValue(ViewState(ViewState.Status.ERROR, error = "Ошибка"))
        }
        return liveData
    }

    suspend fun findMutualFriendsByUsersId(firstName: String, secondName: String): LiveData<ViewState<List<NewUser>>> {
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

            val userResponse = usersApi.getUsersByIds(idString).response

            if (userResponse.isNullOrEmpty()) {
                liveData.postValue(ViewState(ViewState.Status.ERROR, error = "Проверьте правильность полей"))
                return liveData
            }

            val usersId = userResponse.map { it.id.toString() }

             firstUserId = if (isKnowFirstUserId) firstUserId else usersId.first()
             secondUserId = if (isKnowSecondUserId) secondUserId else {
                if (usersId.size == 1) {
                    usersId.first()
                } else {
                    usersId.last()
                }
            }
        }

        val mutualFriendsIds = usersApi.getMutualFriendsId(firstUserId, secondUserId).response

        if (mutualFriendsIds == null) {
            //todo show error here
            Log.d("tag", "some error")
            liveData.postValue(ViewState(ViewState.Status.ERROR, error = "Ошибка"))
            return liveData
        }

        val usersId = mutualFriendsIds.joinToString(",")
        val usersList = usersApi.getUsersWithInfoByIds(usersId).response
        liveData.postValue(ViewState(ViewState.Status.SUCCESS, usersList))

        return liveData
    }
}