package com.lab422.vkanalyzer.utils.settings

import android.content.Context
import com.lab422.vkanalyzer.AnalyzerApp
import com.lab422.vkanalyzer.utils.storage.KeyValueStorage
import com.lab422.vkanalyzer.utils.storage.KeyValueStorageFactory
import com.vk.api.sdk.auth.VKAccessToken

internal class AppSettingsImpl(app: AnalyzerApp) : com.lab422.common.AppSettings {

    companion object {
        private const val PREF_KEY_API_TOKEN = "apiTokenKey"
        private const val PREF_KEY_APP_AUTHORIZATION = "authorizationVkAppKey"
        private const val PREF_KEY_APP_FIRST_LAUNCH = "firstLaunchKey"
        private const val PREF_KEY_APP_SHOW_AUTH_INFO = "firstShowAuthInfo"
    }

    private val appSp = app.getSharedPreferences(com.lab422.common.AppSettings.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val vkSp = app.getSharedPreferences(com.lab422.common.AppSettings.SHARED_PREFERENCES_NAME_VK, Context.MODE_PRIVATE)

    private val keyValueStorage: KeyValueStorage = KeyValueStorageFactory.createStorage(appSp)

    override var vkToken: VKAccessToken?
        get() {
            return VKAccessToken.restore(vkSp)
        }
        set(value) {
            value?.save(vkSp)
        }


    override val isAuthorized: Boolean
        get() = keyValueStorage.getBoolean(PREF_KEY_APP_AUTHORIZATION)

    override fun setAuthorizationFinished() {
        keyValueStorage.set(PREF_KEY_APP_AUTHORIZATION, true)
    }

    override fun logOut() {
        keyValueStorage.set(PREF_KEY_APP_AUTHORIZATION, false)
        keyValueStorage.set(PREF_KEY_API_TOKEN, "")
    }


    override val isFirstLaunch: Boolean
        get() = keyValueStorage.getBoolean(PREF_KEY_APP_FIRST_LAUNCH, true)

    override fun setFirstLaunch() {
        keyValueStorage.set(PREF_KEY_APP_FIRST_LAUNCH, false)
    }

    override val shouldShowAuthInfo: Boolean
        get() = keyValueStorage.getBoolean(PREF_KEY_APP_SHOW_AUTH_INFO, true)

    override fun setShowedAuthInfo() {
        keyValueStorage.set(PREF_KEY_APP_SHOW_AUTH_INFO, false)
    }
}