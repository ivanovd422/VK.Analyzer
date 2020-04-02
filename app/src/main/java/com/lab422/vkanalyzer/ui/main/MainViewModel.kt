package com.lab422.vkanalyzer.ui.main

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amplitude.api.Amplitude
import com.lab422.vkanalyzer.ui.friends.FriendModel
import com.lab422.vkanalyzer.utils.navigator.Navigator
import com.lab422.vkanalyzer.utils.viewState.ViewState
import org.json.JSONException
import org.json.JSONObject

class MainViewModel(
    private val navigator: Navigator
) : ViewModel(), LifecycleObserver {

    private val state: MutableLiveData<ViewState<Unit>> = MutableLiveData()
    private val firstUserNameLiveData: MutableLiveData<String> = MutableLiveData()
    private val secondUserNameLiveData: MutableLiveData<String> = MutableLiveData()

    private var firstFriend: FriendModel? = null
    private var secondFriend: FriendModel? = null

    fun getState(): LiveData<ViewState<Unit>> = state
    fun getFirstUserName(): LiveData<String> = firstUserNameLiveData
    fun getSecondUserName(): LiveData<String> = secondUserNameLiveData

    fun onSearchClicked(firstUserId: String, secondUserId: String) {
        if (firstUserId.isEmpty() || secondUserId.isEmpty()) {
            Amplitude.getInstance().logEvent("get user from friend list");
            state.postValue(
                ViewState(
                    status = ViewState.Status.ERROR,
                    error = "Заполните все поля"
                )
            )
        } else {
            val eventProperties = JSONObject()
            try {
                eventProperties.put("first_user_id", firstUserId)
                eventProperties.put("second_user_id", secondUserId)
            } catch (exception: JSONException) {
            }
            Amplitude.getInstance().logEvent("on search clicked", eventProperties);
            navigator.openMutualListActivity(firstUserId, secondUserId)
        }
    }

    fun onFirstFriendFromList(firstFriend: FriendModel) {
        this.firstFriend = firstFriend
        firstUserNameLiveData.value = firstFriend.name
    }

    fun onSecondFriendFromList(secondFriend: FriendModel) {
        this.secondFriend = secondFriend
        secondUserNameLiveData.value = secondFriend.name
    }

    fun onFirstIdEntered(firstId: String) {
        firstFriend?.let {
            if (it.id.toString() == firstId) {
                firstUserNameLiveData.value = it.name
            } else {
                firstUserNameLiveData.value = ""
            }
        }
    }

    fun onSecondIdEntered(secondId: String) {
        secondFriend?.let {
            if (it.id.toString() == secondId) {
                secondUserNameLiveData.value = it.name
            } else {
                secondUserNameLiveData.value = ""
            }
        }
    }
}