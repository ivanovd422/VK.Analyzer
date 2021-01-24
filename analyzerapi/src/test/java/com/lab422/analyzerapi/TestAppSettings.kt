package com.lab422.analyzerapi

import com.lab422.common.AppSettings
import com.vk.api.sdk.auth.VKAccessToken

class TestAppSettings() : AppSettings {

    override var accessToken: String?
        get() = TestsConfiguration.testToken
        set(value) {}
    override var vkToken: VKAccessToken?
        get() = TODO("not implemented")
        set(value) {}
    override val isAuthorized: Boolean
        get() = TODO("not implemented")

    override fun setAuthorizationFinished() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun logOut() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override val isFirstLaunch: Boolean
        get() = TODO("not implemented")

    override fun setFirstLaunch() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override val shouldShowAuthInfo: Boolean
        get() = TODO("not implemented")

    override fun setShowedAuthInfo() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
