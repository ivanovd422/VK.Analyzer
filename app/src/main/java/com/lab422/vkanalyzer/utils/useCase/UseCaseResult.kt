package com.lab422.vkanalyzer.utils.useCase

sealed class UseCaseResult<out T : Any> {
    class Success<out T : Any>(val data: T) : UseCaseResult<T>()
    class Error(val error: String) : UseCaseResult<Nothing>()
}

fun <T : Any> UseCaseResult<T>.isSuccess(): Boolean {
    return this is UseCaseResult.Success
}

fun <T : Any> UseCaseResult<T>.isError(): Boolean {
    return this is UseCaseResult.Error
}