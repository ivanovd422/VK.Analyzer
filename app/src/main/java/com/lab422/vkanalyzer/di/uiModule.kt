package com.lab422.vkanalyzer.di

import com.lab422.vkanalyzer.ui.friends.FriendsViewModel
import com.lab422.vkanalyzer.ui.loading.LoadingViewModel
import com.lab422.vkanalyzer.ui.login.LoginViewModel
import com.lab422.vkanalyzer.ui.main.MainViewModel
import com.lab422.vkanalyzer.ui.mutualFriends.MutualViewModel
import com.lab422.vkanalyzer.ui.mutualFriends.model.MutualFriendsModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


fun provideUiModule() = module {
    viewModel { LoadingViewModel(get(), get(), get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { (model: MutualFriendsModel) -> MutualViewModel(get(), model, get(), get(), get()) }
    viewModel { FriendsViewModel(get()) }
}