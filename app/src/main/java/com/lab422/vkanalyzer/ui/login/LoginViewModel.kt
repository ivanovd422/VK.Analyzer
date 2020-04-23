package com.lab422.vkanalyzer.ui.login

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lab422.vkanalyzer.utils.SingleLiveEvent
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import com.lab422.vkanalyzer.utils.navigator.Navigator
import com.lab422.common.viewState.ViewState
import com.vk.api.sdk.auth.VKAccessToken


class LoginViewModel(
    private val navigator: Navigator,
    private val settings: com.lab422.common.AppSettings,
    private val tracker: TrackerService
) : ViewModel(), LifecycleObserver {

    private val errorState: SingleLiveEvent<String> = SingleLiveEvent()
    private val state: MutableLiveData<ViewState<Unit>> = MutableLiveData()
    private val dialogAuthInfoState: SingleLiveEvent<Unit> = SingleLiveEvent()

    init {
        if (settings.shouldShowAuthInfo) {
            dialogAuthInfoState.call()
            settings.setShowedAuthInfo()
        }
    }

    fun getState(): LiveData<ViewState<Unit>> = state
    fun getErrorState(): LiveData<String> = errorState
    fun getAuthInfoDialog(): LiveData<Unit> = dialogAuthInfoState

    fun onLoginSuccess(token: VKAccessToken) {
        state.postValue(ViewState(ViewState.Status.SUCCESS))
        settings.vkToken = token
        settings.accessToken = token.accessToken
        settings.setAuthorizationFinished()
        navigator.openMainActivity()
    }

    fun onLoginStart() {
        tracker.onAuthByVkClicked()
        state.postValue(ViewState(ViewState.Status.LOADING))
    }

    fun onLoginFailed() {
        errorState.value = "Ошибка авторизации! Попробуйте снова."
        state.postValue(ViewState(ViewState.Status.ERROR))
    }

    fun onLoginCancelled() {
        errorState.value = "Для работы приложения необходимо авторизироваться!"
        state.postValue(ViewState(ViewState.Status.ERROR))
    }
}