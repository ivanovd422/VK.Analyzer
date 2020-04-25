package com.lab422.analyzerapi

import com.nhaarman.mockitokotlin2.mock

open class TestBase {

    private val apiFactory: ApiFactory by lazy {
        ApiFactory(TestsConfiguration.baseAddress, TestAppSettings(), mock(), mock())
    }

    val usersApi: UsersApi by lazy {
        apiFactory.createAnalyzerApi()
    }
}