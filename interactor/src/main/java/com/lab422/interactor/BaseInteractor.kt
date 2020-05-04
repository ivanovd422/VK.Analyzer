package com.lab422.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lab422.analyzerapi.core.AnalyzerApiException
import com.lab422.common.api.NetworkException
import com.lab422.common.viewState.ViewState

open class BaseInteractor {

    protected suspend fun <T> invokeBlock(block: suspend () -> LiveData<ViewState<T>>): LiveData<ViewState<T>> {
        val liveData: MutableLiveData<ViewState<T>> = MutableLiveData()
        val errorText = "Ошибка сети, попробуйте позже"
        var errorMessage = errorText
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

            if (error is NetworkException) {
                errorMessage = error.message ?: errorText
            }
            liveData.postValue(ViewState(ViewState.Status.ERROR, error = errorMessage))
            return liveData
        }
    }
}