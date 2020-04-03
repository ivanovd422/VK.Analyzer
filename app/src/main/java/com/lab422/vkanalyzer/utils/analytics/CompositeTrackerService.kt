package com.lab422.vkanalyzer.utils.analytics

import android.app.Application


class CompositeTrackerService(private val trackerServices: List<TrackerService>) : TrackerService {

    override fun initialize(context: Application) {
        trackerServices.forEach { it.initialize(context) }
    }

    override fun launch(firstTime: Boolean) {
        trackerServices.forEach { it.launch(firstTime) }
    }

    override fun authByVkSuccess() {
        trackerServices.forEach { it.authByVkSuccess() }
    }

    override fun authByVkFailed() {
        trackerServices.forEach { it.authByVkFailed() }
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

    override fun successLoadMutualFriends() {
        trackerServices.forEach { it.successLoadMutualFriends() }
    }
}