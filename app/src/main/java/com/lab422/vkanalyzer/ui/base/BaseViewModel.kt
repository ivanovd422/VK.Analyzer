package com.lab422.vkanalyzer.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers

abstract class BaseViewModel : ViewModel() {

    protected val networkContext = viewModelScope.coroutineContext + Dispatchers.IO

    inline fun <T> launchOnViewModelScope(crossinline block: suspend () -> LiveData<T>): LiveData<T> {
        return liveData(networkContext) {
            emitSource(block())
        }
    }
}
