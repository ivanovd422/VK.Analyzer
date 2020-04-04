package com.lab422.vkanalyzer.utils.analytics

import android.app.Application
import com.lab422.vkanalyzer.utils.properties.PropertiesUtil
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig


class YandexMetricaService : TrackerService {

    override fun initialize(context: Application) {
        val config = YandexMetricaConfig
            .newConfigBuilder(PropertiesUtil.getYandexMetricaKey(context))
            .build()
        YandexMetrica.activate(context, config)
        YandexMetrica.enableActivityAutoTracking(context)
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

    override fun authByVkSuccess() {
        logEvent(TrackerConstants.EVENT_AUTH_BY_VK_SUCCESS)
    }

    override fun authByVkFailed() {
        logEvent(TrackerConstants.EVENT_AUTH_BY_VK_FAILED)
    }

    override fun authByVkCancelled() {
        logEvent(TrackerConstants.EVENT_AUTH_BY_VK_CANCELED)
    }

    override fun getUserFromFriendListClicked() {
        logEvent(TrackerConstants.EVENT_GET_USER_FROM_FRIEND_LIST)
    }

    override fun onSearchFriendsClicked(firstUser: String, secondUser: String) {
        val eventParameters: MutableMap<String, String> = HashMap()
        eventParameters["first_user_id"] = firstUser
        eventParameters["second_user_id"] = secondUser

        logEvent(TrackerConstants.EVENT_ON_SEARCH_CLICKED, eventParameters)
    }

    override fun openUserByLink(link: String) {
        val eventParameters: MutableMap<String, String> = HashMap()
        eventParameters["open_user_link"] = link

        logEvent(TrackerConstants.EVENT_OPEN_BY_LINK, eventParameters)
    }

    override fun failedLoadUserId(error: String) {
        val eventParameters: MutableMap<String, String> = HashMap()
        eventParameters["error"] = error

        logEvent(TrackerConstants.EVENT_FAILED_LOAD_USERS_ID, eventParameters)
    }

    override fun failedLoadMutualFriends(error: String) {
        val eventParameters: MutableMap<String, String> = HashMap()
        eventParameters["error"] = error

        logEvent(TrackerConstants.EVENT_FAILED_LOAD_MUTUAL_FRIENDS, eventParameters)
    }

    override fun successLoadMutualFriends() {
        logEvent(TrackerConstants.EVENT_SUCCESS_LOAD_MUTUAL_FRIENDS)
    }

    private fun logEvent(eventName: String) {
        YandexMetrica.reportEvent(eventName)
    }

    private fun logEvent(eventName: String, params: MutableMap<String, String>) {
        YandexMetrica.reportEvent(eventName, params.toMap())
    }
}