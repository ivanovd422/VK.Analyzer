package com.lab422.vkanalyzer.utils.logger

import android.util.Log
import com.lab422.common.Logger

internal class LoggerImpl: Logger {

    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun v(tag: String, message: String) {
        Log.v(tag, message)
    }

    override fun w(tag: String, message: String) {
        Log.w(tag, message)
    }

    override fun e(tag: String, throwable: Throwable) {
        Log.e(tag, "$throwable")
//        Crashlytics.logException(throwable)
    }

    override fun e(tag: String, message: String) {
        Log.e(tag, message)
//        Crashlytics.log(message)
    }
}