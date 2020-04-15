package com.lab422.analyzerapi.core

import com.google.gson.Gson
import com.lab422.common.Logger
import com.lab422.common.StringProvider
import okhttp3.Response

internal class ResponseInterceptor(
    private val StringProvider: StringProvider,
    logger: Logger
) : ResponseInterceptorBase(StringProvider, logger) {

    override fun processResponse(response: Response) {
        // TODO: add hack for redirect caching and network error.
        if (!response.isSuccessful) {
            throw AnalyzerApiException.unknownException(
                "System_HTTP_${response.code}",
                StringProvider
            )
        }

        val responseBody = copy(response.body!!, Long.MAX_VALUE)
        val resp = Gson().fromJson(responseBody.charStream(), ApiResponse::class.java)

        if (!resp.success) {
            throw AnalyzerApiException.apiException(
                resp.errorCode
                    ?: "System_Unknown", resp.errorTitle, resp.userDescription
            )
        }
    }

    override fun unknownException(ex: Throwable): Throwable =
        AnalyzerApiException.unknownException(ex, StringProvider)
}