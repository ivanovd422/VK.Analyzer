package com.lab422.vkanalyzer.di

import com.lab422.common.Logger
import com.lab422.common.StringProvider
import com.lab422.common.UserNameValidator
import com.lab422.vkanalyzer.AnalyzerApp
import com.lab422.vkanalyzer.ui.photosNear.dataProvider.UserPhotoDataProvider
import com.lab422.vkanalyzer.ui.photosNear.dataProvider.UserPhotoDataProviderImpl
import com.lab422.vkanalyzer.utils.logger.LoggerImpl
import com.lab422.vkanalyzer.utils.stringProvider.StringProviderImpl
import com.lab422.vkanalyzer.utils.validator.UserNameValidatorImpl
import org.koin.dsl.module

fun provideUtilsModule(app: AnalyzerApp) = module(true) {
    single<StringProvider> { StringProviderImpl(app) }
    single<Logger> { LoggerImpl() }
    single<UserNameValidator> { UserNameValidatorImpl() }
    single<UserPhotoDataProvider> { UserPhotoDataProviderImpl(get()) }
}
