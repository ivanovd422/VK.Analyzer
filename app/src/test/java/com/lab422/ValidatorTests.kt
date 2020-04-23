package com.lab422

import com.lab422.common.UserNameValidator
import com.lab422.vkanalyzer.utils.validator.UserNameValidatorImpl
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class CardDateValidatorTest {

    private lateinit var validator: UserNameValidator

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        validator = UserNameValidatorImpl()
    }

    @Test
    fun `should correct validate`() {
        assertTrue(validator.isId("123"), "should be true")
        assertTrue(validator.isId("https://vk.com/id123"), "should be true")
        assertTrue(validator.isId("https://vk.com/id123"), "should be true")
        assertTrue(validator.isId("vk.com/321312311231"), "should be true")
        assertTrue(validator.isId("vk.com/id321312311231"), "should be true")
        assertTrue(validator.isId("id321312311231"), "should be true")
        assertFalse(validator.isId("321312fsa311231"), "should be false")
        assertFalse(validator.isId("dsadsa312fsa311dsa"), "should be false")
    }
}
