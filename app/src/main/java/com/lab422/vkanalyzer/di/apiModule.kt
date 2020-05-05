package com.lab422.vkanalyzer.di

import com.lab422.analyzerapi.UsersApi
import com.lab422.analyzerapi.ApiFactory
import com.lab422.analyzerapi.PhotosApi
import org.koin.dsl.module

fun provideApiModule() = module {
    single {
        ApiFactory(
            "https://api.vk.com/method/",
            get(),
            get(),
            get()
        )
    }
    single { provideAnalyzerApi(get()) }
    single { providePhotosApi(get()) }
}


fun provideAnalyzerApi(apiFactory: ApiFactory): UsersApi {
    return apiFactory.createUserApi()
}

fun providePhotosApi(apiFactory: ApiFactory): PhotosApi {
    return apiFactory.createPhotosApi()
}