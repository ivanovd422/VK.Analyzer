package com.lab422.analyzerapi.models.users

import com.lab422.analyzerapi.core.ApiResponse

data class UserResponse(
    val response: List<User>
): ApiResponse()
