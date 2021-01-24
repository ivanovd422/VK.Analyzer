package com.lab422.common

import com.vk.api.sdk.auth.VKAccessToken

interface AppSettings {

    companion object {
        const val SHARED_PREFERENCES_NAME = "VK Analyzer"
        const val SHARED_PREFERENCES_NAME_VK = "VK SDK SP"
    }

    var accessToken: String?
    var vkToken: VKAccessToken?
    val isAuthorized: Boolean
    fun setAuthorizationFinished()
    fun logOut()

    val isFirstLaunch: Boolean
    fun setFirstLaunch()

    val shouldShowAuthInfo: Boolean
    fun setShowedAuthInfo()
}
