package com.lab422.vkanalyzer.utils.validator

interface UserNameValidator {
    fun isId(userName: String): Boolean
    fun validate(userName: String): String
}