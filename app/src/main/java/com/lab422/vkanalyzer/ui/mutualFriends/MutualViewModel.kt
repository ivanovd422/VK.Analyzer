package com.lab422.vkanalyzer.ui.mutualFriends

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider.FriendsListDataProvider
import com.lab422.vkanalyzer.ui.mutualFriends.model.MutualFriendsModel
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import com.lab422.vkanalyzer.utils.extensions.debounce
import com.lab422.common.viewState.ViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import com.lab422.analyzerapi.models.users.NewUser
import com.lab422.common.viewState.isSuccess
import com.lab422.interactor.UserInteractor
import com.lab422.vkanalyzer.ui.base.BaseViewModel

class MutualViewModel(
    model: MutualFriendsModel?,
    private val dataProvider: FriendsListDataProvider,
    private val tracker: TrackerService,
    private val userInteractor: UserInteractor
) : BaseViewModel(), LifecycleObserver {

    private val state: MediatorLiveData<ViewState<List<RowDataModel<FriendsListType, *>>>> = MediatorLiveData()
    private val queryLiveData: MutableLiveData<String> = MutableLiveData()
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val userFetchingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var rowData: MutableList<NewUser> = mutableListOf()

    init {
        state.addSource(queryLiveData.debounce(300, viewModelScope)) {
            uiScope.launch {
                val data = dataProvider.filterByQuery(rowData, FriendsListType.Friends, it)
                state.postValue(ViewState(ViewState.Status.SUCCESS, data))
            }
        }

        startLoadingMutualFriends(model)
        userFetchingLiveData.postValue(true)
    }

    fun getState(): LiveData<ViewState<List<RowDataModel<FriendsListType, *>>>> = state

    fun onSearchQueryTyped(text: String) {
        queryLiveData.postValue(text.toLowerCase())
    }

    private fun startLoadingMutualFriends(model: MutualFriendsModel?) {
        if (model == null) {
            showError("Что-то пошло не так")
            return
        }

        state.postValue(ViewState(ViewState.Status.LOADING))
        userFetchingLiveData.switchMap {
            launchOnViewModelScope {
                userInteractor.findMutualFriendsByUsersId(
                    model.firstId,
                    model.secondId
                )
            }
        }.observeForever {
            if (it.isSuccess()) {
                onSuccessLoadUsers(it.data)
            } else {
                showError(it.error)
            }
        }
    }

    private fun showError(textError: String?) {
        val errorMessage = textError ?: "Что-то пошло не так"
        state.postValue(
            ViewState(
                status = ViewState.Status.ERROR,
                error = errorMessage
            )
        )
    }

    private fun onSuccessLoadUsers(result: List<NewUser>?) {
        if (result == null) {
            showError("Что-то пошло не так")
            return
        }
        rowData.clear()
        rowData.addAll(result)

        val data = dataProvider.generateFriendsListData(rowData, FriendsListType.Friends)
        if (data.isEmpty()) {
            showError("Нет общих друзей")
        } else {
            state.postValue(ViewState(ViewState.Status.SUCCESS, data))
        }
        tracker.successLoadMutualFriends(result.size)
    }
}