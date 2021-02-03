package com.lab422.vkanalyzer.utils.analytics

import android.app.Application
import com.amplitude.api.Amplitude
import com.lab422.vkanalyzer.utils.properties.PropertiesUtil
import org.json.JSONException
import org.json.JSONObject

class AmplitudeService : TrackerService {

    override fun initialize(context: Application) {
        Amplitude.getInstance()
            .initialize(context, PropertiesUtil.getAmplitudeKey(context))
            .enableForegroundTracking(context)
    }

    override fun launch(firstTime: Boolean) {
        if (firstTime) {
            logEvent(TrackerConstants.EVENT_FIRST_LAUNCH)
        }
        logEvent(TrackerConstants.EVENT_LAUNCH)
    }

    override fun onAuthByVkClicked() {
        logEvent(TrackerConstants.EVENT_AUTH_BY_VK_CLICKED)
    }

    override fun authByVkSuccess(userId: Int) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put("userId", userId)
        } catch (exception: JSONException) {
        }

        logEvent(TrackerConstants.EVENT_AUTH_BY_VK_SUCCESS, eventProperties)
    }

    override fun authByVkFailed(errorCode: Int) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put("errorCode", errorCode)
        } catch (exception: JSONException) {
        }
        logEvent(TrackerConstants.EVENT_AUTH_BY_VK_FAILED, eventProperties)
    }

    override fun authByVkCancelled() {
        logEvent(TrackerConstants.EVENT_AUTH_BY_VK_CANCELED)
    }

    override fun getUserFromFriendListClicked() {
        logEvent(TrackerConstants.EVENT_GET_USER_FROM_FRIEND_LIST)
    }

    override fun onSearchFriendsClicked(firstUser: String, secondUser: String) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put("first_user_id", firstUser)
            eventProperties.put("second_user_id", secondUser)
        } catch (exception: JSONException) {
        }

        logEvent(TrackerConstants.EVENT_ON_SEARCH_CLICKED, eventProperties)
    }

    override fun openUserByLink(link: String) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put("opened_user_link", link)
        } catch (exception: JSONException) {
        }

        logEvent(TrackerConstants.EVENT_OPEN_BY_LINK, eventProperties)
    }

    override fun failedLoadUserId(error: String) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put("error", error)
        } catch (exception: JSONException) {
        }
        logEvent(TrackerConstants.EVENT_FAILED_LOAD_USERS_ID, eventProperties)
    }

    override fun failedLoadMutualFriends(error: String) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put("error", error)
        } catch (exception: JSONException) {
        }
        logEvent(TrackerConstants.EVENT_FAILED_LOAD_MUTUAL_FRIENDS, eventProperties)
    }

    override fun successLoadMutualFriends(friendsCount: Int) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put("friends count", friendsCount)
        } catch (exception: JSONException) {
        }
        logEvent(TrackerConstants.EVENT_SUCCESS_LOAD_MUTUAL_FRIENDS, eventProperties)
    }

    override fun loadPhotoNearby(isSuccess: Boolean, photosCount: Int?, errorMessage: String?) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put("isSuccess", isSuccess)
            if (photosCount != null) {
                eventProperties.put("photos count", photosCount)
            }

            if (errorMessage != null) {
                eventProperties.put("error", errorMessage)
            }
        } catch (exception: JSONException) {
        }
        logEvent(TrackerConstants.EVENT_NEARBY_PHOTOS_LOADED, eventProperties)
    }

    override fun coordinatesReceived(lat: String, long: String) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put("lat", lat)
            eventProperties.put("long", long)
        } catch (exception: JSONException) {
        }
        logEvent(TrackerConstants.EVENT_CURRENT_COORDINATES_RECEIVED, eventProperties)
    }

    override fun onShareAppClicked() {
        logEvent(TrackerConstants.EVENT_SHARE_APP_CLICKED)
    }

    override fun onSupportClicked() {
        logEvent(TrackerConstants.EVENT_SUPPORT_NEED_CLICKED)
    }

    override fun onPhotoLoadingError(error: String) {
        val eventProperties = JSONObject()
        try {
            eventProperties.put("error", error)
        } catch (exception: JSONException) {
        }
        logEvent(TrackerConstants.EVENT_FULL_SCREEN_PHOTO_ERROR, eventProperties)
    }

    override fun onBoardingFinished() {
        logEvent(TrackerConstants.EVENT_ON_BOARDING_FINISHED)
    }

    override fun onBoardingCancelled() {
        logEvent(TrackerConstants.EVENT_ON_BOARDING_CANCELLED)
    }

    private fun logEvent(eventName: String, jsonObject: JSONObject) {
        Amplitude.getInstance().logEvent(eventName, jsonObject)
    }

    private fun logEvent(eventName: String) {
        Amplitude.getInstance().logEvent(eventName)
    }
}
