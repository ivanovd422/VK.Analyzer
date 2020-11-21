package com.lab422.vkanalyzer.di

import com.lab422.vkanalyzer.ui.friends.FriendsViewModel
import com.lab422.vkanalyzer.ui.friendsList.FriendsListViewModel
import com.lab422.vkanalyzer.ui.loading.LoadingViewModel
import com.lab422.vkanalyzer.ui.login.LoginViewModel
import com.lab422.vkanalyzer.ui.mainScreen.MainViewModel
import com.lab422.vkanalyzer.ui.mutualFriends.MutualViewModel
import com.lab422.vkanalyzer.ui.mutualFriends.model.MutualFriendsModel
import com.lab422.vkanalyzer.ui.onboarding.OnBoardingViewModel
import com.lab422.vkanalyzer.ui.photosNear.PhotosNearViewModel
import com.lab422.vkanalyzer.ui.photosNear.userInfo.UserInfoViewModel
import com.lab422.vkanalyzer.ui.photosNear.userInfo.model.PhotoInfoModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


fun provideUiModule() = module {
    viewModel { LoadingViewModel(get(), get(), get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { FriendsViewModel(get(), get()) }
    viewModel { MainViewModel() }
    viewModel { (model: MutualFriendsModel) -> MutualViewModel(model, get(), get(), get()) }
    viewModel { FriendsListViewModel(get(), get()) }
    viewModel { PhotosNearViewModel(get(), get(), get()) }
    viewModel { (model: PhotoInfoModel) -> UserInfoViewModel(model, get()) }
    viewModel { OnBoardingViewModel() }
}