package com.lab422.vkanalyzer

import android.app.Application
import com.lab422.vkanalyzer.di.provideAppModule
import com.lab422.vkanalyzer.di.provideUiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AnalyzerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
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
            provideUiModule()
        )
}