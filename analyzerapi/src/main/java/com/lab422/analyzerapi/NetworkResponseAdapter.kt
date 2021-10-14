package com.lab422.analyzerapi

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkResponseAdapter : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ) = when (getRawType(returnType)) {
        Call::class.java -> handleCallType(returnType)
        else -> null
    }

    private fun handleCallType(
        returnType: Type
    ): CallAdapter<*, *>? {
        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(callType) != NetworkResponse::class.java) return null

        val resultType = getParameterUpperBound(0, callType as ParameterizedType)

        return NetworkResponseCallAdapter(resultType)
    }
}
