package com.lab422.vkanalyzer.di

import com.lab422.repository.UserRepository
import org.koin.dsl.module

fun provideRepositoryModule() = module {
    single { UserRepository(get()) }
}