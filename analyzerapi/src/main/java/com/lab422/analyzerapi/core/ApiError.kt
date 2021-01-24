package com.lab422.analyzerapi.core

data class ApiError(
    val code: String,
    val description: String?,
    val userDescription: String?,
    val title: String?
)
