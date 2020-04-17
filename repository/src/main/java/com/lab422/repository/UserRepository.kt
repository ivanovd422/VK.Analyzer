package com.lab422.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lab422.analyzerapi.UsersApi
import com.lab422.analyzerapi.models.friendsList.convertToUser
import com.lab422.analyzerapi.models.users.User
import com.lab422.common.viewState.ViewState

class UserRepository constructor(
    private val usersApi: UsersApi
) {

    suspend fun getFriendsList(): LiveData<ViewState<List<User>>> {
        val liveData = MutableLiveData<ViewState<List<User>>>()
        val friendsList = mutableListOf<User>()
        val result = usersApi.getFriendsList()

        if (result.success) {
            result.response.items.forEach { friendsList.add(it.convertToUser()) }
            liveData.value = ViewState(ViewState.Status.SUCCESS, friendsList)
        } else {
            liveData.value = ViewState(ViewState.Status.ERROR, error = "Ошибка")
        }
        return liveData
    }
}