package com.lab422.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lab422.analyzerapi.core.AnalyzerApiException
import com.lab422.common.api.NetworkException
import com.lab422.common.viewState.ViewState

//todo delete it5
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
                if (error.code == "1") {
                    errorMessage = "Попробуйте повторить запрос позже."
                }
                if (error.code == "10") {
                    errorMessage = "Попробуйте повторить запрос позже"
                }
                if (error.code == "18") {
                    errorMessage = "Страница пользователя была удалена или заблокирована"
                }

                if (error.code == "15") {
                    errorMessage = "Доступ запрещён"
                    if (
                        error.message.isNullOrEmpty().not() &&
                        error.message == "Access denied: you are in users blacklist"
                    ) {
                        errorMessage = "Пользователь добавил Вас в чёрный список"
                    }
                }
            }

            if (error is NetworkException) {
                errorMessage = error.message ?: errorText
            }
            liveData.postValue(ViewState(ViewState.Status.ERROR, error = errorMessage, internalError = error.message))
            return liveData
        }
    }
}
