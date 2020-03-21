package com.lab422.vkanalyzer.ui.main

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amplitude.api.Amplitude
import com.lab422.vkanalyzer.utils.navigator.Navigator
import com.lab422.vkanalyzer.utils.viewState.ViewState
import org.json.JSONException
import org.json.JSONObject

class MainViewModel(
    private val navigator: Navigator
) : ViewModel(), LifecycleObserver {

    private val state: MutableLiveData<ViewState<Unit>> = MutableLiveData()

    fun getState(): LiveData<ViewState<Unit>> = state

    fun onSearchClicked(firstUserId: String, secondUserId: String) {
        if (firstUserId.isEmpty() || secondUserId.isEmpty()) {
            Amplitude.getInstance().logEvent("get user from friend list");
            state.postValue(
                ViewState(
                    status = ViewState.Status.ERROR,
                    error = "Заполните все поля"
                )
            )
        } else {
            val eventProperties = JSONObject()
            try {
                eventProperties.put("first_user_id", firstUserId)
                eventProperties.put("second_user_id", secondUserId)
            } catch (exception: JSONException) {
            }
            Amplitude.getInstance().logEvent("on search clicked", eventProperties);
            navigator.openMutualListActivity(firstUserId, secondUserId)
        }
    }
}