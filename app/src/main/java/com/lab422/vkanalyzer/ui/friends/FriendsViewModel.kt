package com.lab422.vkanalyzer.ui.friends

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lab422.vkanalyzer.ui.friendsList.FriendModel
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import com.lab422.vkanalyzer.utils.navigator.Navigator
import com.lab422.common.viewState.ViewState

class FriendsViewModel(
    private val navigator: Navigator,
    private val tracker: TrackerService
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
            tracker.getUserFromFriendListClicked()
            state.postValue(
                ViewState(
                    status = ViewState.Status.ERROR,
                    error = "Заполните все поля"
                )
            )
        } else {
            tracker.onSearchFriendsClicked(firstUserId, secondUserId)
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