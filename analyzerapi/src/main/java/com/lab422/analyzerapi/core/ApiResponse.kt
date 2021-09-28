package com.lab422.analyzerapi.core

import com.google.gson.annotations.SerializedName

open class ApiResponse(
    val error: Error? = null
)

data class Error(
    @SerializedName("error_code")
    val errorCode: Int,
    @SerializedName("error_msg")
    val errorMsg: String,
    @SerializedName("request_params")
    val requestParams: List<RequestParam>
)

data class RequestParam(
    val key: String,
    val value: String
)
