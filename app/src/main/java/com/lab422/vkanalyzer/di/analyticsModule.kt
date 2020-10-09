package com.lab422.vkanalyzer.di

import com.lab422.vkanalyzer.utils.analytics.AmplitudeService
import com.lab422.vkanalyzer.utils.analytics.AppsflyerService
import com.lab422.vkanalyzer.utils.analytics.CompositeTrackerService
import com.lab422.vkanalyzer.utils.analytics.FacebookService
import com.lab422.vkanalyzer.utils.analytics.FirebaseAnalyticsService
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import com.lab422.vkanalyzer.utils.analytics.YandexMetricaService
import org.koin.dsl.module

fun provideAnalyticsModule() = module {
    single { AmplitudeService() }
    single { YandexMetricaService() }
    single { FirebaseAnalyticsService() }
    single { AppsflyerService() }
    single { FacebookService() }
    single<TrackerService> { CompositeTrackerService(
        listOf(
            get(AmplitudeService::class.java),
            get(YandexMetricaService::class.java),
            get(FirebaseAnalyticsService::class.java),
            get(AppsflyerService::class.java),
            get(FacebookService::class.java)
        )
    ) }
}