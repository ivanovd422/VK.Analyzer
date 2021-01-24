package com.lab422.analyzerapi

import com.google.gson.GsonBuilder
import com.lab422.analyzerapi.core.OAuthInterceptor
import com.lab422.analyzerapi.core.ResponseInterceptor
import com.lab422.common.AppSettings
import com.lab422.common.BuildConfig
import com.lab422.common.Logger
import com.lab422.common.StringProvider
import java.util.concurrent.TimeUnit
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiFactory(
    private val baseAddress: String,
    private val appSettings: AppSettings,
    private val stringProvider: StringProvider,
    private val logger: Logger
) {

    private val timeoutSeconds: Long = 90

    private inline fun <reified T> createApi(): T = retrofit.create<T>(T::class.java)

    private val retrofit: Retrofit by lazy {
        val gson = GsonBuilder().create()

        val builder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseAddress)

        builder
            .client(okHttpClient)
            .build()
    }

    private val dispatcher: Dispatcher by lazy {
        val dispatcher = Dispatcher()
        dispatcher.maxRequestsPerHost = dispatcher.maxRequests
        dispatcher
    }

    private val loggingInterceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        val clientBuilder = OkHttpClient.Builder()
            .dispatcher(dispatcher)
            .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .callTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
            .apply {
                val token = appSettings.accessToken
                if (token.isNullOrEmpty().not()) {
                    addInterceptor(OAuthInterceptor(token!!))
                }
            }

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            clientBuilder.addInterceptor(loggingInterceptor)
        }

        clientBuilder.addInterceptor(ResponseInterceptor(stringProvider, logger))
        clientBuilder.build()
    }

    fun createUserApi(): UsersApi = createApi()

    fun createPhotosApi(): PhotosApi = createApi()
}
