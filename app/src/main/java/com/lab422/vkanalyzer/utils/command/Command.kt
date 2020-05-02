package com.lab422.vkanalyzer.utils.command

import android.content.Context

interface Command {
    fun execute(context: Context): Any
}