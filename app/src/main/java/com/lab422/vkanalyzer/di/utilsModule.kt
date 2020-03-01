package com.lab422.vkanalyzer.di

import com.lab422.vkanalyzer.AnalyzerApp
import com.lab422.vkanalyzer.utils.stringProvider.StringProvider
import com.lab422.vkanalyzer.utils.stringProvider.StringProviderImpl
import org.koin.dsl.module

fun provideUtilsModule(app: AnalyzerApp) = module(true) {
    single<StringProvider> { StringProviderImpl(app) }
}