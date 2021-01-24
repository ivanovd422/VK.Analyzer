package com.lab422.vkanalyzer.utils.analytics

import android.app.Application
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.AppsFlyerLibCore.LOG_TAG
import com.lab422.vkanalyzer.utils.properties.PropertiesUtil

class AppsflyerService : TrackerService {

    override fun initialize(context: Application) {
        val conversionDataListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                data?.let { cvData ->
                    cvData.map {
                        Log.i(LOG_TAG, "conversion_attribute:  ${it.key} = ${it.value}")
                    }
                }
            }

            override fun onConversionDataFail(error: String?) {
                Log.e(LOG_TAG, "error onAttributionFailure :  $error")
            }

            override fun onAppOpenAttribution(data: MutableMap<String, String>?) {
                data?.map {
                    Log.d(LOG_TAG, "onAppOpen_attribute: ${it.key} = ${it.value}")
                }
            }

            override fun onAttributionFailure(error: String?) {
                Log.e(LOG_TAG, "error onAttributionFailure :  $error")
            }
        }
        val key = PropertiesUtil.getAppsflyerKey(context)

        AppsFlyerLib.getInstance().apply {
            init(key, conversionDataListener, context)
            startTracking(context)
        }
    }

    override fun launch(firstTime: Boolean) = Unit
    override fun authByVkSuccess(userId: Int) = Unit
    override fun authByVkFailed(errorCode: Int) = Unit
    override fun authByVkCancelled() = Unit
    override fun getUserFromFriendListClicked() = Unit
    override fun onSearchFriendsClicked(firstUser: String, secondUser: String) = Unit
    override fun onAuthByVkClicked() = Unit
    override fun openUserByLink(link: String) = Unit
    override fun failedLoadUserId(error: String) = Unit
    override fun failedLoadMutualFriends(error: String) = Unit
    override fun successLoadMutualFriends(friendsCount: Int) = Unit
    override fun loadPhotoNearby(isSuccess: Boolean, photosCount: Int?, errorMessage: String?) = Unit
    override fun coordinatesReceived(lat: String, long: String) = Unit
    override fun onShareAppClicked() = Unit
    override fun onSupportClicked() = Unit
    override fun onPhotoLoadingError(error: String) = Unit
}
