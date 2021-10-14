package com.lab422.analyzerapi

sealed class NetworkResponse<out T> {

    companion object {
        fun <T> success(value: T): NetworkResponse<T> = Success.Value(value)

        fun <T> failure(error: Throwable, value: T? = null): NetworkResponse<T> = Failure(error, value)
    }
}

sealed class Success<T> : NetworkResponse<T>() {

    abstract val value: T

    data class Value<T>(override val value: T) : Success<T>()
}

class Failure<T>(
    val error: Throwable,
    val value: T? = null
) : NetworkResponse<T>()

inline fun <T> NetworkResponse<T>.onResult(
    isSuccess: (value: T) -> Unit = {},
    isFailure: (exception: Throwable, value: T?) -> Unit = { _, _ -> }
) {
    when (this) {
        is Success -> isSuccess(value)
        is Failure -> isFailure(error, value)
    }
}

inline fun <R, T> NetworkResponse<T>.map(transform: (value: T) -> R) =
    when (this) {
        is Success -> NetworkResponse.success(transform(value))
        is Failure -> NetworkResponse.failure(error)
    }

inline fun <T, R> T.catchResult(block: T.() -> R): NetworkResponse<R> {
    return try {
        NetworkResponse.success(block())
    } catch (e: Throwable) {
        NetworkResponse.failure(e)
    }
}

inline fun <R, T> NetworkResponse<T>.flatMap(transform: (value: T) -> NetworkResponse<R>) =
    when (this) {
        is Success -> transform(value)
        is Failure -> NetworkResponse.failure(error)
    }

fun <T> NetworkResponse<T>.isSuccess() = this is Success<T>

fun <T> NetworkResponse<T>.isFailure() = this is Failure<T>

fun <T> NetworkResponse<T>.value(): T? {
    return when (this) {
        is Success -> this.value
        is Failure -> this.value
    }
}
