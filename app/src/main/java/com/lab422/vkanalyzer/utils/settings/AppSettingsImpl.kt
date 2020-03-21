package com.lab422.vkanalyzer.utils.settings

import android.content.Context
import com.lab422.vkanalyzer.AnalyzerApp
import com.lab422.vkanalyzer.utils.storage.KeyValueStorage
import com.lab422.vkanalyzer.utils.storage.KeyValueStorageFactory
import com.vk.api.sdk.auth.VKAccessToken

internal class AppSettingsImpl(app: AnalyzerApp) : AppSettings {

    companion object {
        private const val PREF_KEY_API_TOKEN = "apiTokenKey"
        private const val PREF_KEY_APP_AUTHORIZATION = "authorizationVkAppKey"
        private const val PREF_KEY_APP_FIRST_LAUNCH = "firstLaunchKey"
    }

    private val appSp = app.getSharedPreferences(AppSettings.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val vkSp = app.getSharedPreferences(AppSettings.SHARED_PREFERENCES_NAME_VK, Context.MODE_PRIVATE)

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
}