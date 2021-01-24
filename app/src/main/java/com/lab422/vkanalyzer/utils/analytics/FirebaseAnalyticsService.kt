package com.lab422.vkanalyzer.utils.analytics

import android.app.Application
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseAnalyticsService : TrackerService {

    private lateinit var analytics: FirebaseAnalytics

    override fun initialize(context: Application) {
        analytics = FirebaseAnalytics.getInstance(context)
    }

    override fun launch(firstTime: Boolean) {
        FirebaseAnalytics.Param.ITEM_ID
        if (firstTime) {
            logEvent(TrackerConstants.EVENT_FIRST_LAUNCH)
        }
        logEvent(TrackerConstants.EVENT_LAUNCH)
    }

    override fun authByVkSuccess(userId: Int) {
        val bundle = Bundle()
        bundle.putString("userId", userId.toString())
        logEvent(TrackerConstants.EVENT_AUTH_BY_VK_SUCCESS, bundle)
    }

    override fun authByVkFailed(errorCode: Int) {
        val bundle = Bundle()
        bundle.putString("errorCode", errorCode.toString())
        logEvent(TrackerConstants.EVENT_AUTH_BY_VK_FAILED, bundle)
    }

    override fun authByVkCancelled() {
        logEvent(TrackerConstants.EVENT_AUTH_BY_VK_CANCELED)
    }

    override fun getUserFromFriendListClicked() {
        logEvent(TrackerConstants.EVENT_GET_USER_FROM_FRIEND_LIST)
    }

    override fun onSearchFriendsClicked(firstUser: String, secondUser: String) {
        val bundle = Bundle()
        bundle.putString("firstUser", firstUser)
        bundle.putString("secondUser", secondUser)

        logEvent(TrackerConstants.EVENT_ON_SEARCH_CLICKED, bundle)
    }

    override fun onAuthByVkClicked() {
        logEvent(TrackerConstants.EVENT_AUTH_BY_VK_CLICKED)
    }

    override fun openUserByLink(link: String) {
        val bundle = Bundle()
        bundle.putString("opened_user_link", link)
        logEvent(TrackerConstants.EVENT_OPEN_BY_LINK, bundle)
    }

    override fun failedLoadUserId(error: String) {
        val bundle = Bundle()
        bundle.putString("error", error)
        logEvent(TrackerConstants.EVENT_FAILED_LOAD_USERS_ID, bundle)
    }

    override fun failedLoadMutualFriends(error: String) {
        val bundle = Bundle()
        bundle.putString("error", error)
        logEvent(TrackerConstants.EVENT_FAILED_LOAD_MUTUAL_FRIENDS, bundle)
    }

    override fun successLoadMutualFriends(friendsCount: Int) {
        val bundle = Bundle()
        bundle.putString("friends count", friendsCount.toString())
        logEvent(TrackerConstants.EVENT_SUCCESS_LOAD_MUTUAL_FRIENDS, bundle)
    }

    override fun loadPhotoNearby(isSuccess: Boolean, photosCount: Int?, errorMessage: String?) {
        val bundle = Bundle()
        bundle.putBoolean("isSuccess", isSuccess)
        if (photosCount != null) {
            bundle.putString("photos count", photosCount.toString())
        }
        if (errorMessage != null) {
            bundle.putString("error", errorMessage)
        }
        logEvent(TrackerConstants.EVENT_NEARBY_PHOTOS_LOADED, bundle)
    }

    override fun coordinatesReceived(lat: String, long: String) {
        val bundle = Bundle()
        bundle.putString("lat", lat)
        bundle.putString("long", long)
        logEvent(TrackerConstants.EVENT_CURRENT_COORDINATES_RECEIVED, bundle)
    }

    override fun onShareAppClicked() {
        logEvent(TrackerConstants.EVENT_SHARE_APP_CLICKED, null)
    }

    override fun onSupportClicked() {
        logEvent(TrackerConstants.EVENT_SUPPORT_NEED_CLICKED, null)
    }

    override fun onPhotoLoadingError(error: String) {
        //
    }

    private fun logEvent(eventName: String, bundle: Bundle? = null) {
        analytics.logEvent(eventName, bundle)
    }
}
