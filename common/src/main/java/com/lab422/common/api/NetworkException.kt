package com.lab422.common.api

open class NetworkException(
    networkTitle: String,
    networkDescription: String,
    cause: Throwable
) :
    ApiException("System_Network_${cause::class.java.simpleName}", networkTitle, networkDescription, cause)

class NetworkTimeoutException(
    networkTitle: String,
    networkDescription: String,
    cause: Throwable
) : NetworkException(networkTitle, networkDescription, cause)
