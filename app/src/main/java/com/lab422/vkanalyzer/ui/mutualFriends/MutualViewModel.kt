package com.lab422.vkanalyzer.ui.mutualFriends

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab422.analyzerapi.models.users.NewUser
import com.lab422.analyzerapi.onResult
import com.lab422.common.viewState.ViewState
import com.lab422.interactor.UserInteractor
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.ui.mutualFriends.list.dataProvider.FriendsListDataProvider
import com.lab422.vkanalyzer.ui.mutualFriends.model.MutualFriendsModel
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MutualViewModel(
    model: MutualFriendsModel?,
    private val dataProvider: FriendsListDataProvider,
    private val tracker: TrackerService,
    private val userInteractor: UserInteractor
) : ViewModel(), LifecycleObserver {

    private val _state: MutableLiveData<ViewState<List<RowDataModel<FriendsListType, *>>>> = MutableLiveData()
    val state: LiveData<ViewState<List<RowDataModel<FriendsListType, *>>>> = _state

    private var rowData: MutableList<NewUser> = mutableListOf()

    private var friendsFilteredJob: Job? = null

    init {
        startLoadingMutualFriends(model)
    }

    override fun onCleared() {
        super.onCleared()
        friendsFilteredJob?.cancel()
    }

    fun onSearchQueryTyped(text: String) {
        friendsFilteredJob?.cancel()
        friendsFilteredJob = viewModelScope.launch {
            delay(300)
            val data = dataProvider.filterByQuery(rowData, FriendsListType.Friends, text)
            _state.value = ViewState(ViewState.Status.SUCCESS, data)
        }
    }

    private fun startLoadingMutualFriends(model: MutualFriendsModel?) {
        if (model == null) {
            showError("Что-то пошло не так")
            return
        }

        _state.value = ViewState(ViewState.Status.LOADING)

        viewModelScope.launch {
            userInteractor.findMutualFriendsByUsersId(
                model.firstId,
                model.secondId
            ).onResult(
                isSuccess = {
                    onSuccessLoadUsers(it)
                },
                isFailure = { _, _ ->
                    showError(null)
                }
            )
        }
    }

    private fun showError(textError: String?) {
        val errorMessage = textError ?: "Что-то пошло не так"
        _state.value = ViewState(
            status = ViewState.Status.ERROR,
            error = errorMessage
        )
    }

    private fun onSuccessLoadUsers(result: List<NewUser>) {
        rowData.clear()
        rowData.addAll(result)

        val data = dataProvider.generateFriendsListData(rowData, FriendsListType.Friends)
        if (data.isEmpty()) {
            showError("Нет общих друзей")
        } else {
            _state.value = ViewState(ViewState.Status.SUCCESS, data)
        }
        tracker.successLoadMutualFriends(result.size)
    }
}
