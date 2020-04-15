package com.lab422.analyzerapi.models.friendsList

import java.io.Serializable

data class Response(
    val count: Int,
    val items: List<Item>
) : Serializable