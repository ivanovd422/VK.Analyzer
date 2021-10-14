package com.lab422.vkanalyzer.ui.userInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab422.analyzerapi.models.users.NewUser
import com.lab422.analyzerapi.onResult
import com.lab422.common.viewState.ViewState
import com.lab422.interactor.UserInteractor
import com.lab422.vkanalyzer.ui.userInfo.model.PhotoInfoModel
import com.lab422.vkanalyzer.ui.userInfo.model.UserInfoModel
import kotlinx.coroutines.launch

class UserInfoViewModel(
    private val photoModel: PhotoInfoModel,
    private val userInteractor: UserInteractor
) : ViewModel() {

    private val _userInfoState: MutableLiveData<ViewState<UserInfoModel>> = MutableLiveData()
    val userInfoState: LiveData<ViewState<UserInfoModel>> = _userInfoState

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        _userInfoState.value = ViewState(ViewState.Status.LOADING)
        viewModelScope.launch {
            userInteractor.getUserInfoById(photoModel.userId)
                .onResult(
                    isSuccess = {
                        _userInfoState.value = ViewState(
                            ViewState.Status.SUCCESS,
                            it?.convertToUserInfoModel(
                                photoModel,
                                photoModel.clickedPhotoUrl
                            )
                        )
                    },
                    isFailure = { _, _ ->
                        _userInfoState.value = ViewState(ViewState.Status.ERROR, error = "Ошибка")
                    }
                )
        }
    }
}

private fun NewUser.convertToUserInfoModel(photoModel: PhotoInfoModel, clickedPhotoUrl: String): UserInfoModel =
    UserInfoModel(
        id.toString(),
        "$firstName $lastName",
        online != 0,
        photoUrl ?: "",
        photoModel.lat,
        photoModel.long,
        clickedPhotoUrl
    )
