package com.lab422.vkanalyzer.utils.analytics

import android.app.Application

interface TrackerService {

    fun initialize(context: Application)
    fun launch(firstTime: Boolean)
    fun authByVkSuccess(userId: Int)
    fun authByVkFailed(errorCode: Int)
    fun authByVkCancelled()
    fun getUserFromFriendListClicked()
    fun onSearchFriendsClicked(firstUser: String, secondUser: String)
    fun onAuthByVkClicked()
    fun openUserByLink(link: String)
    fun failedLoadUserId(error: String)
    fun failedLoadMutualFriends(error: String)
    fun successLoadMutualFriends(friendsCount: Int)
    fun loadPhotoNearby(isSuccess: Boolean, photosCount: Int? = null, errorMessage: String? = null)
    fun coordinatesReceived(lat: String, long: String)
    fun onShareAppClicked()
    fun onSupportClicked()
    fun onPhotoLoadingError(error: String)
    fun onBoardingFinished()
    fun onBoardingCancelled()
}