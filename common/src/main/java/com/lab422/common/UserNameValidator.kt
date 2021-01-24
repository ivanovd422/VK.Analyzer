package com.lab422.common

interface UserNameValidator {
    fun isId(userName: String): Boolean
    fun validate(userName: String): String
}
