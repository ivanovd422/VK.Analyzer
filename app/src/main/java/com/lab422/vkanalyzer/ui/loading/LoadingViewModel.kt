package com.lab422.vkanalyzer.ui.loading

import androidx.lifecycle.ViewModel
import com.lab422.common.AppSettings
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import com.lab422.vkanalyzer.utils.navigator.Navigator

class LoadingViewModel(
    private val appSettings: AppSettings,
    private val navigator: Navigator,
    private val tracker: TrackerService
) : ViewModel() {

    fun onCreated() {
        val isFirstLaunch = appSettings.isFirstLaunch
        if (isFirstLaunch) {
            appSettings.setFirstLaunch()
        }

        tracker.launch(isFirstLaunch)

        if (appSettings.isOnBoardingFinished.not()) {
            navigator.openOnBoarding()
            return
        }

        if (appSettings.isAuthorized && appSettings.isTokenValid()) {
            navigator.openMainActivity()
        } else {
            navigator.openLoginActivity()
        }
    }
}
