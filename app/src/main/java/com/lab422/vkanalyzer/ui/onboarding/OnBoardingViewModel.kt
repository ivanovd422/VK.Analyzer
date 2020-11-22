package com.lab422.vkanalyzer.ui.onboarding

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.mutualFriends.list.adapter.FriendsListType
import com.lab422.vkanalyzer.ui.mutualFriends.model.UserViewModel
import com.lab422.vkanalyzer.ui.onboarding.adapter.OnBoardingScreensProvider
import com.lab422.vkanalyzer.ui.onboarding.adapter.OnBoardingType
import com.lab422.vkanalyzer.ui.onboarding.adapter.OnBoardingViewData

class OnBoardingViewModel : ViewModel() {

    private companion object {
        const val TOUCH_DELAY_TIME = 200
        const val MIN_X_FORWARD_COORDINATE_IN_PERCENT = 0.3
    }

    private var isForwardDirection: Boolean = true
    private var pausedTime: Long? = null
    private var forwardScreenMinX: Int = 0
    private var currentScreenPosition = 0
    private val screensCount = OnBoardingScreensProvider.getScreensCount()

    val onBoardingPositionEvent: MutableLiveData<Int> = MutableLiveData()
    val onBoardingImages: MutableLiveData<List<RowDataModel<OnBoardingType, *>>> = MutableLiveData()
    val onScrollForwardDirection: MutableLiveData<Boolean> = MutableLiveData()

    init {
        onBoardingImages.value = generateViewData(OnBoardingScreensProvider.getOnBoardingScreenViewData())
        onBoardingPositionEvent.value = currentScreenPosition
    }

    fun setScreenWidth(screenWidth: Int) {
        forwardScreenMinX = (screenWidth * MIN_X_FORWARD_COORDINATE_IN_PERCENT).toInt()
    }

    fun onPauseStory(x: Float) {
        isForwardDirection = calculateIsForwardDirectionClick(x.toInt())
        pausedTime = getCurrentTime()
    }

    fun onResumeStory() {
        handleResumeEvent(isShouldScrollNext(pausedTime), isForwardDirection)
    }

    fun onStoryEnd(isLastStory: Boolean) {
        if (currentScreenPosition == screensCount - 1 || isLastStory) {
            Log.d("myTag", "should cancel on boarding")
            return
        } else {
            currentScreenPosition++
            onBoardingPositionEvent.value = currentScreenPosition
        }
    }

    private fun getCurrentTime(): Long = System.currentTimeMillis()

    private fun isShouldScrollNext(startTime: Long?): Boolean {
        if (startTime == null) return true
        val result = getCurrentTime() - startTime
        return result < TOUCH_DELAY_TIME
    }

    private fun calculateIsForwardDirectionClick(clickX: Int): Boolean {
        return clickX > forwardScreenMinX
    }

    private fun handleResumeEvent(isShouldChangeScreen: Boolean, isForwardDirection: Boolean) {
        if (isShouldChangeScreen.not()) return

        onScrollForwardDirection.value = isForwardDirection

        if (isForwardDirection) {
            if (currentScreenPosition == screensCount - 1) {
                Log.d("myTag", "should cancel on boarding")
                return
            } else {
                currentScreenPosition++
                onBoardingPositionEvent.value = currentScreenPosition
            }
        } else {
            // reverse direction case
            if (currentScreenPosition <= 0) {
                // onBoardingPositionEvent.value = currentScreenPosition
            } else {
                currentScreenPosition--
                onBoardingPositionEvent.value = currentScreenPosition
            }
        }
    }

    private fun generateViewData(viewData: List<OnBoardingViewData>): List<RowDataModel<OnBoardingType, *>> =
        viewData.map { data ->
            RowDataModel(
                OnBoardingType.OnBoarding,
                data.title.toString(),
                data
            )
        }
}