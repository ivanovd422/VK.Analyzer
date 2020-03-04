package com.lab422.vkanalyzer.di

import com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider.FriendsListDataProvider
import com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider.FriendsDataProviderImpl
import com.lab422.vkanalyzer.utils.validator.UserNameValidator
import com.lab422.vkanalyzer.utils.validator.UserNameValidatorImpl
import org.koin.dsl.module

fun provideMutualFriendsModule() = module {
    single<FriendsListDataProvider> { FriendsDataProviderImpl() }
    single<UserNameValidator> { UserNameValidatorImpl() }
}