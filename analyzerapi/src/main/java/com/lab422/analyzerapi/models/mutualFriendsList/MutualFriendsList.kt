package com.lab422.analyzerapi.models.mutualFriendsList

import com.lab422.analyzerapi.core.ApiResponse

data class MutualFriendsList(
    val response: List<Long>
) : ApiResponse()
