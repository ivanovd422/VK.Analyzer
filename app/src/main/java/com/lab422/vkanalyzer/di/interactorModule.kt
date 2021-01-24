package com.lab422.vkanalyzer.di

import com.lab422.interactor.PhotosInteractor
import com.lab422.interactor.UserInteractor
import org.koin.dsl.module

fun provideInteractorModule() = module {
    single { UserInteractor(get(), get()) }
    single { PhotosInteractor(get()) }
}
