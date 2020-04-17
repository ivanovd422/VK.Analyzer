package com.lab422.common.viewState

class ViewState<D>(
    val status: Status,
    val data: D? = null,
    val error: String? = null
) {
    enum class Status {
        LOADING, SUCCESS, ERROR
    }
}

inline fun <reified T> ViewState<T>.isSuccess(): Boolean {
    return this.status == ViewState.Status.SUCCESS
}

inline fun <reified T> ViewState<T>.isLoading(): Boolean {
    return this.status == ViewState.Status.LOADING
}

inline fun <reified T> ViewState<T>.isError(): Boolean {
    return this.status == ViewState.Status.ERROR
}