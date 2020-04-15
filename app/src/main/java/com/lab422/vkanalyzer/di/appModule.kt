package com.lab422.vkanalyzer.di

import com.lab422.vkanalyzer.AnalyzerApp
import com.lab422.vkanalyzer.utils.navigator.Navigator
import com.lab422.common.AppSettings
import com.lab422.vkanalyzer.utils.settings.AppSettingsImpl
import org.koin.dsl.module

fun provideAppModule(app: AnalyzerApp) = module(true) {

    single<AppSettings> {
        AppSettingsImpl(app)
    }

    single { Navigator(get()) }
}