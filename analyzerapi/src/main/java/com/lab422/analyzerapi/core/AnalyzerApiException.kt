package com.lab422.analyzerapi.core

import com.lab422.analyzerapi.R
import com.lab422.common.StringProvider
import com.lab422.common.api.ApiException

class AnalyzerApiException : ApiException {

    constructor(code: String, title: String?, userDescription: String?) : super(code, title, userDescription)

    constructor(code: String, title: String?, userDescription: String, cause: Throwable) : super(
        code,
        title,
        userDescription,
        cause
    )

    companion object {

        fun apiException(code: String, title: String?, userDescription: String?): ApiException =
            AnalyzerApiException(code, title, userDescription)

        fun unknownException(cause: Throwable, localStringProvider: StringProvider): ApiException =
            AnalyzerApiException(
                "System_${cause::class.java.simpleName}",
                localStringProvider.getString(R.string.default_title) ?: "",
                localStringProvider.getString(R.string.default_message),
                cause
            )

        fun unknownException(code: String, localStringProvider: StringProvider): ApiException =
            AnalyzerApiException(
                code,
                localStringProvider.getString(R.string.default_title),
                localStringProvider.getString(R.string.default_message)
            )
    }
}
