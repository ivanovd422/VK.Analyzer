package com.lab422.analyzerapi.core

open class ApiResponse (
    val errors: Array<ApiError>? = null,
    val success: Boolean = true
){

    val error: ApiError?
        get() = errors?.get(0)

    val errorCode: String?
        get() = error?.code

    open val errorDescription: String?
        get() = error?.description

    val userDescription: String?
        get() = error?.userDescription ?: error?.description

    open val errorTitle: String?
        get() = error?.title
}