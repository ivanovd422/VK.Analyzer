package com.lab422.common.api

import com.lab422.common.AnalyzerApiException
import com.lab422.common.R
import com.lab422.common.StringProvider

open class ApiException : AnalyzerApiException {

    val code: String

    constructor(code: String, title: String?, userDescription: String?) : super(title, userDescription) {
        this.code = code
    }

    constructor(code: String, title: String?, userDescription: String, cause: Throwable) : super(
        title,
        userDescription,
        cause
    ) {
        this.code = code
    }

    companion object {

        fun networkException(cause: Throwable, stringProvider: StringProvider): ApiException =
            NetworkException(
                stringProvider.getString(R.string.networkError_title),
                stringProvider.getString(R.string.networkError_message),
                cause
            )

        fun networkTimeoutException(cause: Throwable, stringProvider: StringProvider): ApiException =
            NetworkTimeoutException(
                stringProvider.getString(R.string.networkTimeoutError_title),
                stringProvider.getString(R.string.networkTimeoutError_message),
                cause
            )

        fun unknownException(cause: Throwable, stringProvider: StringProvider): ApiException =
            ApiException(
                "System_${cause::class.java.simpleName}",
                stringProvider.getString(R.string.default_title),
                stringProvider.getString(R.string.default_message),
                cause
            )

        fun unknownException(code: String, stringProvider: StringProvider): ApiException =
            ApiException(
                code,
                stringProvider.getString(R.string.default_title),
                stringProvider.getString(R.string.default_message)
            )
    }
}

val AnalyzerApiException.isNetworkError: Boolean
    get() = this is NetworkException
