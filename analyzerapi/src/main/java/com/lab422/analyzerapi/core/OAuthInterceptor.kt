package com.lab422.analyzerapi.core

import okhttp3.FormBody
import okhttp3.Interceptor

class OAuthInterceptor(private val accessToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        val bodyBuilder: FormBody.Builder = FormBody.Builder()

        bodyBuilder
            .add("access_token", accessToken)
            .add("v", "5.103")

        request = request.newBuilder().post(bodyBuilder.build()).build()

        return chain.proceed(request)
    }
}
