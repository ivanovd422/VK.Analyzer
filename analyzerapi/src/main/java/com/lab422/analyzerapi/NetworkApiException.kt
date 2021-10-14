package com.lab422.analyzerapi

import java.io.IOException

class NetworkApiException(
    val description: String?,
    val code: Int?,
    val error: Throwable?
) : IOException()
