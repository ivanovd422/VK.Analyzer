package com.lab422.vkanalyzer

import android.app.Application
import com.lab422.vkanalyzer.di.provideAnalyticsModule
import com.lab422.vkanalyzer.di.provideApiModule
import com.lab422.vkanalyzer.di.provideAppModule
import com.lab422.vkanalyzer.di.provideMutualFriendsModule
import com.lab422.vkanalyzer.di.provideUiModule
import com.lab422.vkanalyzer.di.provideUtilsModule
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AnalyzerApp : Application() {

    private val trackerService: TrackerService by inject()

    override fun onCreate() {
        super.onCreate()
        initKoin()
        startAnalytics()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@AnalyzerApp)
            modules(configureDiModules())
        }
    }

    private fun configureDiModules() =
        listOf(
            provideAppModule(this),
            provideAnalyticsModule(),
            provideUtilsModule(this),
            provideMutualFriendsModule(),
            provideUiModule(),
            provideApiModule()
        )

    private fun startAnalytics() {
        trackerService.initialize(this)
    }
}