package com.lab422.vkanalyzer.utils.analytics

import android.app.Application
import com.facebook.FacebookSdk

class FacebookService : TrackerService {
    override fun initialize(context: Application) {
        // FacebookSdk.setAutoLogAppEventsEnabled(true)
    }

    override fun launch(firstTime: Boolean) = Unit

    override fun authByVkSuccess(userId: Int) = Unit

    override fun authByVkFailed(errorCode: Int) = Unit

    override fun authByVkCancelled() = Unit

    override fun getUserFromFriendListClicked() = Unit

    override fun onSearchFriendsClicked(firstUser: String, secondUser: String) = Unit

    override fun onAuthByVkClicked() = Unit

    override fun openUserByLink(link: String) = Unit

    override fun failedLoadUserId(error: String) = Unit

    override fun failedLoadMutualFriends(error: String) = Unit

    override fun successLoadMutualFriends(friendsCount: Int) = Unit

    override fun loadPhotoNearby(isSuccess: Boolean, photosCount: Int?, errorMessage: String?) = Unit

    override fun coordinatesReceived(lat: String, long: String) = Unit

    override fun onShareAppClicked() = Unit

    override fun onSupportClicked() = Unit

    override fun onPhotoLoadingError(error: String) = Unit

    override fun onBoardingFinished()  = Unit

    override fun onBoardingCancelled()  = Unit
}