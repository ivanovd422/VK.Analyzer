package com.lab422.analyzerapi

import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class FriendsTest : TestBase() {

    @Before
    fun setUp() = runBlocking {}

    @Test
    fun `mutual friends should be not empty`() = runBlocking {
        val result = usersApi.getMutualFriendsId("6492", "2745")
        assertTrue(result.response.isNullOrEmpty().not(), "should be not empty")
    }

    @Test
    fun `friends should be not empty`() = runBlocking {
        val result = usersApi.getFriendsList().response
        assertTrue(result.items.isNullOrEmpty().not(), "should be not empty")
    }

    @Test
    fun `users list should be not empty`() = runBlocking {
        val result = usersApi.getUsersByIds("34,243,952,1568").response
        assertTrue(result.isNullOrEmpty().not(), "should be not empty")
    }
}
