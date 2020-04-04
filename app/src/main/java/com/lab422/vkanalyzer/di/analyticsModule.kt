package com.lab422.vkanalyzer.di

import com.lab422.vkanalyzer.utils.analytics.AmplitudeService
import com.lab422.vkanalyzer.utils.analytics.CompositeTrackerService
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import com.lab422.vkanalyzer.utils.analytics.YandexMetricaService
import org.koin.dsl.module

fun provideAnalyticsModule() = module {
    single { AmplitudeService() }
    single { YandexMetricaService() }
    single<TrackerService> { CompositeTrackerService(
        listOf(
            get(AmplitudeService::class.java),
            get(YandexMetricaService::class.java)
        )
    ) }
}