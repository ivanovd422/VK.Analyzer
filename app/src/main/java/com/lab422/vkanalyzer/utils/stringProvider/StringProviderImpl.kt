package com.lab422.vkanalyzer.utils.stringProvider

import android.content.Context

class StringProviderImpl(private val context: Context) : com.lab422.common.StringProvider {

    override fun getString(byId: Int, vararg formatArgs: Any): String {
        return context.resources.getString(byId, formatArgs)
    }

    override fun stringById(stringId: Int): String {
        return context.resources.getString(stringId)
    }

    override fun stringById(stringId: Int, vararg formatArgs: Any): String {
        return context.resources.getString(stringId, *formatArgs)
    }

    override fun stringWithQuantityById(stringId: Int, quantity: Int): String {
        return context.resources.getQuantityString(stringId, quantity, quantity)
    }

    override fun stringArrayById(arrayStringId: Int): Array<out String> {
        return context.resources.getStringArray(arrayStringId)
    }
}