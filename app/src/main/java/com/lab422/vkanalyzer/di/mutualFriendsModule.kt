package com.lab422.vkanalyzer.di

import com.lab422.vkanalyzer.AnalyzerApp
import com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider.MutualFriendsDataProvider
import com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider.MutualFriendsDataProviderImpl
import com.lab422.vkanalyzer.utils.navigator.Navigator
import com.lab422.vkanalyzer.utils.settings.AppSettings
import com.lab422.vkanalyzer.utils.settings.AppSettingsImpl
import org.koin.dsl.module

fun provideMutualFriendsModule() = module {

    single<MutualFriendsDataProvider> {
        MutualFriendsDataProviderImpl()
    }
}