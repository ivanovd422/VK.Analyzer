package com.lab422.vkanalyzer.ui.loading

import androidx.lifecycle.ViewModel
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import com.lab422.vkanalyzer.utils.navigator.Navigator
import com.lab422.common.AppSettings


class LoadingViewModel(
    private val appSettings: com.lab422.common.AppSettings,
    private val navigator: Navigator,
    private val tracker: TrackerService
) : ViewModel() {

    fun onCreated() {
        val isFirstLaunch = appSettings.isFirstLaunch
        if (isFirstLaunch) {
            appSettings.setFirstLaunch()
        }

        tracker.launch(isFirstLaunch)

        if (appSettings.isAuthorized && isTokenValid()) {
            navigator.openMainActivity()
        } else {
            navigator.openLoginActivity()
        }
    }

    private fun isTokenValid(): Boolean {
        val token = appSettings.vkToken ?: return false
        if (appSettings.accessToken.isNullOrEmpty()) return false
        return token.isValid
    }
}