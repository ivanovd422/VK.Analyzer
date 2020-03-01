package com.lab422.vkanalyzer.utils.stringProvider

interface StringProvider{

    fun getString(byId: Int, vararg formatArgs: Any): String

    fun stringById(stringId: Int): String

    fun stringById(stringId: Int, vararg formatArgs: Any): String

    fun stringArrayById(arrayStringId: Int): Array<out String>

    fun stringWithQuantityById(stringId: Int, quantity: Int): String
}