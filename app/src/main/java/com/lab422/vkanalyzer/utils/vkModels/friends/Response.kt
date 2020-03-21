package com.lab422.vkanalyzer.utils.vkModels.friends

import java.io.Serializable

data class Response(
    val count: Int,
    val items: List<Item>
) : Serializable