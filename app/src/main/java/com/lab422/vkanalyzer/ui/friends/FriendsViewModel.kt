package com.lab422.vkanalyzer.ui.friends

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.lab422.analyzerapi.models.users.NewUser
import com.lab422.interactor.UserInteractor
import com.lab422.vkanalyzer.ui.base.BaseViewModel
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider.FriendsListDataProvider
import com.lab422.vkanalyzer.utils.extensions.debounce
import com.lab422.common.viewState.ViewState
import com.lab422.common.viewState.isSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch



class FriendsViewModel(
    private val dataProvider: FriendsListDataProvider,
    private val userInteractor: UserInteractor
) : BaseViewModel(), LifecycleObserver {

    private val state: MediatorLiveData<ViewState<List<RowDataModel<FriendsListType, *>>>> = MediatorLiveData()
    private val queryLiveData: MutableLiveData<String> = MutableLiveData()
    private var rowData: MutableList<NewUser> = mutableListOf()
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val userFetchingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        state.addSource(queryLiveData.debounce(300, viewModelScope)) {
            uiScope.launch {
                val data = dataProvider.filterByQuery(rowData, FriendsListType.SelectableFriends, it)
                state.postValue(ViewState(ViewState.Status.SUCCESS, data))
            }
        }

        userFetchingLiveData.switchMap {
            launchOnViewModelScope {
                userInteractor.getFriendsList()
            }
        }.observeForever {
            if (it.isSuccess()) {
                onSuccessLoadUsers(it.data)
            } else {
                showError("some error")
            }
        }

        startLoadingFriendsList()
    }

    fun getFriendsState(): LiveData<ViewState<List<RowDataModel<FriendsListType, *>>>> = state

    fun onSearchQueryTyped(text: String) {
        queryLiveData.postValue(text.toLowerCase())
    }

    private fun startLoadingFriendsList() {
        userFetchingLiveData.postValue(true)
    }

    private fun showError(textError: String) {
        state.postValue(
            ViewState(
                status = ViewState.Status.ERROR,
                error = textError
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        state.removeSource(queryLiveData)
        viewModelJob.cancel()
    }

    private fun onSuccessLoadUsers(result: List<NewUser>?) {
        if (result == null) return
        rowData.clear()
        rowData.addAll(result)

        val data = dataProvider.generateFriendsListData(rowData, FriendsListType.SelectableFriends)
        if (data.isEmpty()) {
            showError("Нет общих друзей")
        } else {
            state.postValue(ViewState(ViewState.Status.SUCCESS, data))
        }
    }
}