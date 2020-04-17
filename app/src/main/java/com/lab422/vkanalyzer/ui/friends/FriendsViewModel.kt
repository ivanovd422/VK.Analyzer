package com.lab422.vkanalyzer.ui.friends

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.lab422.analyzerapi.models.users.User
import com.lab422.repository.UserRepository
import com.lab422.vkanalyzer.ui.base.BaseViewModel
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider.FriendsListDataProvider
import com.lab422.vkanalyzer.utils.extensions.debounce
import com.lab422.common.viewState.ViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch



class FriendsViewModel(
    private val dataProvider: FriendsListDataProvider,
    private val userRepository: UserRepository
) : BaseViewModel(), LifecycleObserver {

    private val state: MediatorLiveData<ViewState<List<RowDataModel<FriendsListType, *>>>> = MediatorLiveData()
    private val queryLiveData: MutableLiveData<String> = MutableLiveData()

    private var rowData: MutableList<User> = mutableListOf()

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val usersListLiveData: LiveData<ViewState<List<User>>>
    private val userFetchingLiveData: MutableLiveData<Boolean> = MutableLiveData()


    init {
        startLoadingFriendsList()

        state.addSource(queryLiveData.debounce(300, viewModelScope)) {
            uiScope.launch {
                val data = dataProvider.filterByQuery(rowData, FriendsListType.SelectableFriends, it)
                state.postValue(ViewState(ViewState.Status.SUCCESS, data))
            }
        }

        usersListLiveData = userFetchingLiveData.switchMap {
            launchOnViewModelScope {
                userRepository.getFriendsList()
            }
        }
    }

    fun getFriendsState(): LiveData<ViewState<List<RowDataModel<FriendsListType, *>>>> = state

    fun onSearchQueryTyped(text: String) {
        queryLiveData.postValue(text.toLowerCase())
    }

    private fun startLoadingFriendsList() {
        userFetchingLiveData.postValue(true)
/*        VK.execute(FriendsCommand(), object : VKApiCallback<List<User>> {
            override fun fail(error: Exception) {
                if (error is VKApiExecutionException) {
                    error.code
                }
                showError(error.message ?: "some error")
            }

            override fun success(result: List<User>) {
                onSuccessLoadUsers(result)
            }
        })*/
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

    private fun onSuccessLoadUsers(result: List<User>) {
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