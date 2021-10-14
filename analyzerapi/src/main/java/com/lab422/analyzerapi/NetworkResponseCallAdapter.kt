package com.lab422.analyzerapi

import android.accounts.NetworkErrorException
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class NetworkResponseCallAdapter(
    private val type: Type,
) : CallAdapter<Type, Call<NetworkResponse<Type>>> {
    override fun responseType(): Type = type
    override fun adapt(call: Call<Type>): Call<NetworkResponse<Type>> =
        ResultCall(
            call,
            type,
        )
}

abstract class CallDelegate<TIn, TOut>(
    protected val proxy: Call<TIn>
) : Call<TOut> {
    override fun execute(): Response<TOut> = throw NotImplementedError()
    final override fun enqueue(callback: Callback<TOut>) = enqueueImpl(callback)
    final override fun clone(): Call<TOut> = cloneImpl()

    override fun cancel() = proxy.cancel()
    override fun request(): Request = proxy.request()
    override fun isExecuted() = proxy.isExecuted
    override fun isCanceled() = proxy.isCanceled

    abstract fun enqueueImpl(callback: Callback<TOut>)
    abstract fun cloneImpl(): Call<TOut>
}

class ResultCall<T>(
    proxy: Call<T>,
    private val type: Type,
) : CallDelegate<T, NetworkResponse<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<NetworkResponse<T>>) =
        proxy.enqueue(
            ResultMapperCallback(
                callback,
                this@ResultCall,
                type,
            )
        )

    override fun cloneImpl() = ResultCall(
        proxy.clone(),
        type,
    )

    override fun timeout(): Timeout = Timeout.NONE
}

class ResultMapperCallback<T>(
    private val enqueueCallback: Callback<NetworkResponse<T>>,
    private val resultCall: ResultCall<T>,
    private val type: Type,
) : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        val result = toNetworkResponse(response, type)
        enqueueCallback.onResponse(resultCall, Response.success(result))
    }

    override fun onFailure(call: Call<T>, error: Throwable) {
        val result = Failure<T>(error)
        enqueueCallback.onResponse(resultCall, Response.success(result))
    }

    private fun <T> toNetworkResponse(response: Response<T>, type: Type): NetworkResponse<T> = with(response) {
        catchResult {
            val errorBodyString = errorBody()?.string()

            if (response.isSuccessful) {
                try {
                    type.handleUnit<T>() ?: body() ?: throw NetworkErrorException("Response body is null")
                } catch (error: Throwable) {
                    throw NetworkApiException(errorBodyString, response.code(), error)
                }
            } else {
                val error = NetworkApiException(errorBodyString, response.code(), null)
                throw error
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> Type.handleUnit(): T? {
    if (this != Unit::class.java) return null

    return try {
        Unit as T
    } catch (e: Exception) {
        throw NetworkErrorException("Expected Unit", e)
    }
}

