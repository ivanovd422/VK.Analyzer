package com.lab422.vkanalyzer.utils.analytics

import android.app.Application

class CompositeTrackerService(private val trackerServices: List<TrackerService>) : TrackerService {

    override fun initialize(context: Application) {
        trackerServices.forEach { it.initialize(context) }
    }

    override fun launch(firstTime: Boolean) {
        trackerServices.forEach { it.launch(firstTime) }
    }

    override fun authByVkSuccess(userId: Int) {
        trackerServices.forEach { it.authByVkSuccess(userId) }
    }

    override fun authByVkFailed(errorCode: Int) {
        trackerServices.forEach { it.authByVkFailed(errorCode) }
    }

    override fun authByVkCancelled() {
        trackerServices.forEach { it.authByVkCancelled() }
    }

    override fun getUserFromFriendListClicked() {
        trackerServices.forEach { it.getUserFromFriendListClicked() }
    }

    override fun onSearchFriendsClicked(firstUser: String, secondUser: String) {
        trackerServices.forEach { it.onSearchFriendsClicked(firstUser, secondUser) }
    }

    override fun onAuthByVkClicked() {
        trackerServices.forEach { it.onAuthByVkClicked() }
    }

    override fun openUserByLink(link: String) {
        trackerServices.forEach { it.openUserByLink(link) }
    }

    override fun failedLoadUserId(error: String) {
        trackerServices.forEach { it.failedLoadUserId(error) }
    }

    override fun failedLoadMutualFriends(error: String) {
        trackerServices.forEach { it.failedLoadMutualFriends(error) }
    }

    override fun successLoadMutualFriends(friendsCount: Int) {
        trackerServices.forEach { it.successLoadMutualFriends(friendsCount) }
    }

    override fun loadPhotoNearby(isSuccess: Boolean, photosCount: Int?, errorMessage: String?) {
        trackerServices.forEach { it.loadPhotoNearby(isSuccess, photosCount, errorMessage) }
    }

    override fun coordinatesReceived(lat: String, long: String) {
        trackerServices.forEach { it.coordinatesReceived(lat, long) }
    }

    override fun onShareAppClicked() {
        trackerServices.forEach { it.onShareAppClicked() }
    }

    override fun onSupportClicked() {
        trackerServices.forEach { it.onSupportClicked() }
    }

    override fun onPhotoLoadingError(error: String) {
        trackerServices.forEach { it.onPhotoLoadingError(error) }
    }
}
