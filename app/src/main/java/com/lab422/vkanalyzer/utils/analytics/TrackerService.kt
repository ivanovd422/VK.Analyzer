package com.lab422.vkanalyzer.utils.analytics

import android.app.Application

interface TrackerService {

    fun initialize(context: Application)
    fun launch(firstTime: Boolean)
    fun authByVkSuccess()
    fun authByVkFailed()
    fun authByVkCancelled()
    fun getUserFromFriendListClicked()
    fun onSearchFriendsClicked(firstUser: String, secondUser: String)
    fun onAuthByVkClicked()
    fun openUserByLink(link: String)
    fun failedLoadUserId(error: String)
    fun failedLoadMutualFriends(error: String)
    fun successLoadMutualFriends()
}