package com.lab422.vkanalyzer.ui.loading

import androidx.lifecycle.ViewModel
import com.amplitude.api.Amplitude
import com.lab422.vkanalyzer.utils.navigator.Navigator
import com.lab422.vkanalyzer.utils.settings.AppSettings


class LoadingViewModel(
    private val appSettings: AppSettings,
    private val navigator: Navigator
) : ViewModel() {

    fun onCreated() {
        if (appSettings.isFirstLaunch) {
            Amplitude.getInstance().logEvent("first launch");
            appSettings.setFirstLaunch()
        }

        Amplitude.getInstance().logEvent("launch");

        if (appSettings.isAuthorized && isTokenValid()) {
            navigator.openMainActivity()
        } else {
            navigator.openLoginActivity()
        }
    }

    private fun isTokenValid(): Boolean {
        val token = appSettings.vkToken ?: return false
        return token.isValid
    }
}