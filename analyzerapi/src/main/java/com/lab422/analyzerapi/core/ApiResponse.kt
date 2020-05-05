package com.lab422.analyzerapi.core

open class ApiResponse(
    val error: Error? = null
)

data class Error(
    val error_code: Int,
    val error_msg: String,
    val request_params: List<RequestParam>
)

data class RequestParam(
    val key: String,
    val value: String
)