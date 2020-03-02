package com.lab422.vkanalyzer.di

import com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider.MutualFriendsDataProvider
import com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider.MutualFriendsDataProviderImpl
import com.lab422.vkanalyzer.utils.validator.UserNameValidator
import com.lab422.vkanalyzer.utils.validator.UserNameValidatorImpl
import org.koin.dsl.module

fun provideMutualFriendsModule() = module {
    single<MutualFriendsDataProvider> { MutualFriendsDataProviderImpl() }
    single<UserNameValidator> { UserNameValidatorImpl() }
}