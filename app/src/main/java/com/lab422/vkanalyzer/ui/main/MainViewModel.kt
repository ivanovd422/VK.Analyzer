package com.lab422.vkanalyzer.ui.main

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lab422.vkanalyzer.utils.navigator.Navigator
import com.lab422.vkanalyzer.utils.viewState.ViewState

class MainViewModel(
    private val navigator: Navigator
) : ViewModel(), LifecycleObserver {

    private val state: MutableLiveData<ViewState<Unit>> = MutableLiveData()

    fun getState(): LiveData<ViewState<Unit>> = state

    fun onSearchClicked(firstUserId: String, secondUserId: String) {
        if (firstUserId.isEmpty() || secondUserId.isEmpty()) {
            state.postValue(
                ViewState(
                    status = ViewState.Status.ERROR,
                    error =  "Заполните все поля"
                ))
        } else {
            navigator.openMutualListActivity(firstUserId, secondUserId)
        }
    }
}