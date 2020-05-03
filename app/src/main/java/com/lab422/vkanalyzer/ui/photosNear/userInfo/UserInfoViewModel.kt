package com.lab422.vkanalyzer.ui.photosNear.userInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.lab422.analyzerapi.models.users.NewUser
import com.lab422.common.viewState.ViewState
import com.lab422.common.viewState.isSuccess
import com.lab422.interactor.UserInteractor
import com.lab422.vkanalyzer.ui.base.BaseViewModel
import com.lab422.vkanalyzer.ui.photosNear.userInfo.model.PhotoInfoModel
import com.lab422.vkanalyzer.ui.photosNear.userInfo.model.UserInfoModel

class UserInfoViewModel(
    private val photoModel: PhotoInfoModel,
    private val userInteractor: UserInteractor
) : BaseViewModel() {

    private val userInfoState: MediatorLiveData<ViewState<UserInfoModel>> = MediatorLiveData()
    fun getUserInfoState(): LiveData<ViewState<UserInfoModel>> = userInfoState

    private val userFetchingLiveData: MutableLiveData<ViewState<UserInfoModel>> = MutableLiveData()

    init {
        userInfoState.addSource(userFetchingLiveData) { userInfoState.value = it }
        userInfoState.addSource(launchOnViewModelScope {
            userInteractor.getUserInfoById(photoModel.userId)
        }.map {
            return@map if (it.isSuccess() && it.data != null) {
                ViewState(ViewState.Status.SUCCESS, it.data!!.convertToUserInfoModel(photoModel))
            } else {
                ViewState(ViewState.Status.ERROR, error = "Ошибка")
            }
        }) {
            userInfoState.value = it
        }

        userFetchingLiveData.value = ViewState(ViewState.Status.LOADING)
    }
}

private fun NewUser.convertToUserInfoModel(photoModel: PhotoInfoModel): UserInfoModel =
    UserInfoModel(
        id.toString(),
        "$first_name $last_name",
        online != 0,
        photoUrl ?: "",
        photoModel.lat,
        photoModel.long
    )