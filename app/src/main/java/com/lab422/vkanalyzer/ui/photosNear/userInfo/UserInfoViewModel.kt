package com.lab422.vkanalyzer.ui.photosNear.userInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.lab422.analyzerapi.models.users.NewUser
import com.lab422.common.viewState.ViewState
import com.lab422.common.viewState.isSuccess
import com.lab422.interactor.UserInteractor
import com.lab422.vkanalyzer.ui.base.BaseViewModel
import com.lab422.vkanalyzer.ui.photosNear.userInfo.model.UserInfoModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class UserInfoViewModel(
    private val userId: String,
    private val userInteractor: UserInteractor
) : BaseViewModel() {

    private val viewModelJob = SupervisorJob()
    private val jobScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    private val userInfoState: MutableLiveData<ViewState<UserInfoModel>> = MutableLiveData()
    fun getUserInfoState(): LiveData<ViewState<UserInfoModel>> = userInfoState


    init {
        userInfoState.value = ViewState(ViewState.Status.LOADING)


        jobScope.launch {
            val viewState = userInteractor.getUserInfoById(userId)
            if (viewState.isSuccess() && viewState.data != null) {
                val data = viewState.data!!
                userInfoState.postValue(
                    ViewState(ViewState.Status.SUCCESS, data.convertToUserInfoModel())
                )
            }
        }

    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    private fun showError(textError: String) {
        userInfoState.postValue(
            ViewState(
                status = ViewState.Status.ERROR,
                error = textError
            )
        )
    }

}

private fun NewUser.convertToUserInfoModel() : UserInfoModel =
    UserInfoModel(
        id.toString(),
        "$first_name $last_name",
        online != 0,
        photoUrl ?: ""
    )