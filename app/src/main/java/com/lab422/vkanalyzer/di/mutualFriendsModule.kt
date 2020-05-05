package com.lab422.vkanalyzer.di

import com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider.FriendsListDataProvider
import com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider.FriendsDataProviderImpl
import org.koin.dsl.module

fun provideMutualFriendsModule() = module {
    single<FriendsListDataProvider> { FriendsDataProviderImpl() }
}