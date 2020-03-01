package com.lab422.vkanalyzer.ui.main

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lab422.vkanalyzer.utils.SingleLiveEvent
import com.lab422.vkanalyzer.utils.navigator.Navigator
import com.lab422.vkanalyzer.utils.requests.VKUsersCommand
import com.lab422.vkanalyzer.utils.settings.AppSettings
import com.lab422.vkanalyzer.utils.viewState.ViewState
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.exceptions.VKApiException
import com.vk.api.sdk.exceptions.VKApiExecutionException

class MainViewModel(
    private val navigator: Navigator
) : ViewModel(), LifecycleObserver {

    private val state: MutableLiveData<ViewState<Unit>> = MutableLiveData()

    fun getState(): LiveData<ViewState<Unit>> = state


    fun onSearchClicked(firstUserId: String, secondUserId: String) {
        navigator.openMutualListActivity(*, *)
    }
}