package com.lab422.vkanalyzer.ui.login

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab422.vkanalyzer.utils.SingleLiveEvent
import com.lab422.vkanalyzer.utils.navigator.Navigator
import com.lab422.vkanalyzer.utils.settings.AppSettings
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class LoginViewModel(
    private val navigator: Navigator,
    private val settings: AppSettings
) : ViewModel(), LifecycleObserver {

    private val errorState: SingleLiveEvent<String> = SingleLiveEvent()

    fun getErrorState(): LiveData<String> = errorState

    fun onLoginSuccess(token: VKAccessToken) {
        settings.vkToken = token
        settings.setAuthorizationFinished()
        navigator.openMainActivity()
    }

    fun onLoginFailed() {
        errorState.value = "Ошибка авторизации! Попробуйте снова."
    }

    fun onLoginCancelled() {
        errorState.value = "Для пользования приложением необходимо авторизироваться!"
    }
}