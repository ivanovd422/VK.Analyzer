package com.lab422.analyzerapi.core

import okhttp3.Interceptor

class OAuthInterceptor(private val accessToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", accessToken).build()

        return chain.proceed(request)
    }
}