package com.lab422.vkanalyzer

import android.app.Application
import com.amplitude.api.Amplitude
import com.lab422.vkanalyzer.di.provideAppModule
import com.lab422.vkanalyzer.di.provideMutualFriendsModule
import com.lab422.vkanalyzer.di.provideUiModule
import com.lab422.vkanalyzer.di.provideUtilsModule
import com.lab422.vkanalyzer.utils.properties.PropertiesUtil
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AnalyzerApp : Application() {

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
            provideUtilsModule(this),
            provideMutualFriendsModule(),
            provideUiModule()
        )

    private fun startAnalytics() {
        val key =
        Amplitude.getInstance()
            .initialize(this, PropertiesUtil.getAmplitudeKey(this))
            .enableForegroundTracking(this)
    }
}