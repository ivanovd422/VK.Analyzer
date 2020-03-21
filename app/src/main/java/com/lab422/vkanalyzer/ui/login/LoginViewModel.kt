package com.lab422.vkanalyzer.ui.login

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amplitude.api.Amplitude
import com.lab422.vkanalyzer.utils.SingleLiveEvent
import com.lab422.vkanalyzer.utils.navigator.Navigator
import com.lab422.vkanalyzer.utils.settings.AppSettings
import com.lab422.vkanalyzer.utils.viewState.ViewState
import com.vk.api.sdk.auth.VKAccessToken


class LoginViewModel(
    private val navigator: Navigator,
    private val settings: AppSettings
) : ViewModel(), LifecycleObserver {

    private val errorState: SingleLiveEvent<String> = SingleLiveEvent()
    private val state: MutableLiveData<ViewState<Unit>> = MutableLiveData()

    fun getState(): LiveData<ViewState<Unit>> = state
    fun getErrorState(): LiveData<String> = errorState

    fun onLoginSuccess(token: VKAccessToken) {
        state.postValue(ViewState(ViewState.Status.SUCCESS))
        settings.vkToken = token
        settings.setAuthorizationFinished()
        navigator.openMainActivity()
    }

    fun onLoginStart() {
        Amplitude.getInstance().logEvent("auth by vk clicked");
        state.postValue(ViewState(ViewState.Status.LOADING))
    }

    fun onLoginFailed() {
        errorState.value = "Ошибка авторизации! Попробуйте снова."
        state.postValue(ViewState(ViewState.Status.ERROR))
    }

    fun onLoginCancelled() {
        errorState.value = "Для пользования приложением необходимо авторизироваться!"
        state.postValue(ViewState(ViewState.Status.ERROR))
    }
}