package com.lab422.common

import java.io.IOException
import java.util.concurrent.CancellationException

interface AnalyzerApiExceptionType

open class AnalyzerApiException : IOException, AnalyzerApiExceptionType {

    val title: String?
    val userDescription: String?
        get() = message

    constructor(userDescription: String?) : super(userDescription ?: "") {
        title = null
    }

    constructor(title: String?, userDescription: String?) : super(userDescription ?: "") {
        this.title = title
    }

    protected constructor(title: String?, userDescription: String, cause: Throwable) : super(userDescription, cause) {
        this.title = title
    }
}

fun catchAppLevelExceptions(cause: Throwable, logger: Logger, tag: String) {
    if (cause !is AnalyzerApiExceptionType) {
        if (cause is IOException) {
            logger.d(tag, "IO level exception: ${cause.message}")
        } else if (cause is CancellationException) {
            logger.e(tag, "cancellation exception: ${cause.message}")
        } else {
            throw cause
        }
    } else {
        logger.d(tag, "App level exception: ${cause.message}")
    }
}