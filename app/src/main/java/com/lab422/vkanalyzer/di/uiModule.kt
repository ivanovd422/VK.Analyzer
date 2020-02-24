package com.lab422.vkanalyzer.di

import com.lab422.vkanalyzer.ui.loading.LoadingViewModel
import com.lab422.vkanalyzer.ui.login.LoginViewModel
import com.lab422.vkanalyzer.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


fun provideUiModule() = module {
    viewModel { LoadingViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { MainViewModel(get()) }
}