package com.lab422.analyzerapi.core

import com.google.gson.Gson
import com.lab422.common.Logger
import com.lab422.common.StringProvider
import okhttp3.Response

internal class ResponseInterceptor(
    private val stringProvider: StringProvider,
    logger: Logger
) : ResponseInterceptorBase(stringProvider, logger) {

    override fun processResponse(response: Response) {

        if (!response.isSuccessful) {
            throw AnalyzerApiException.unknownException(
                "System_HTTP_${response.code}",
                stringProvider
            )
        }

        val responseBody = copy(response.body!!, Long.MAX_VALUE)
        val resp = Gson().fromJson(responseBody.charStream(), ApiResponse::class.java)

        val apiError = resp.error
        if (apiError != null) {
            throw AnalyzerApiException.apiException(
                code = apiError.errorCode.toString(),
                title = apiError.errorMsg,
                userDescription = apiError.errorMsg
            )
        }
    }

    override fun unknownException(ex: Throwable): Throwable =
        AnalyzerApiException.unknownException(ex, stringProvider)
}
