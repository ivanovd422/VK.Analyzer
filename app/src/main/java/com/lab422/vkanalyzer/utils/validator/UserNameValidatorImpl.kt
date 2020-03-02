package com.lab422.vkanalyzer.utils.validator

import androidx.core.text.isDigitsOnly

class UserNameValidatorImpl : UserNameValidator {

    companion object {
        private const val prefix1 = "https://vk.com/id"
        private const val prefix2 = "https://"
        private const val prefix3 = "vk.com/"
        private const val prefix4 = "id"
    }

    private val numbersPattern = Regex("[0-9]+")

    override fun isId(userName: String): Boolean {
        val validatedString = validate(userName)
        return validatedString.matches(numbersPattern)
    }

    override fun validate(userName: String): String {
        //todo add good regex
        var trimmed = userName.replace(" ", "")
        trimmed = trimmed.replace(prefix1, "")
        trimmed = trimmed.replace(prefix2, "")
        trimmed = trimmed.replace(prefix3, "")
        trimmed = trimmed.replace(prefix4, "")

        return trimmed
    }
}