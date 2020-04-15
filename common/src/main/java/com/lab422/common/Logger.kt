package com.lab422.common

interface Logger {
    fun e(tag: String, throwable: Throwable)
    fun e(tag: String, message: String)
    fun w(tag: String, message: String)
    fun v(tag: String, message: String)
    fun d(tag: String, message: String)
}