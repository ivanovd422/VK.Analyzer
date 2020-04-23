package com.lab422.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lab422.analyzerapi.UsersApi
import com.lab422.analyzerapi.models.friendsList.convertToUser
import com.lab422.analyzerapi.models.users.NewUser
import com.lab422.common.viewState.ViewState

class UserRepository constructor(
    private val usersApi: UsersApi
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
}