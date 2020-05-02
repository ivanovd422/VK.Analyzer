package com.lab422.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lab422.analyzerapi.core.AnalyzerApiException
import com.lab422.common.viewState.ViewState

open class BaseInteractor {

    protected suspend fun <T> invokeBlock(block: suspend () -> LiveData<ViewState<T>>): LiveData<ViewState<T>> {
        val liveData: MutableLiveData<ViewState<T>> = MutableLiveData()
        var errorMessage = "Ошибка сети, попробуйте позже"
        try {
            return block()
        } catch (error: Throwable) {
            if (error is AnalyzerApiException) {

                if (error.code == "30") {
                    errorMessage = "Один из профилей скрыт"
                }

                if (error.code == "113") {
                    errorMessage = "Проверьте правильность полей"
                }
            }
            liveData.postValue(ViewState(ViewState.Status.ERROR, error = errorMessage))
            return liveData
        }
    }
}