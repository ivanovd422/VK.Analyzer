package com.lab422.vkanalyzer.utils.storage

interface KeyValueStorage {
    fun <T> set(key: String, value: T)
    fun getString(key: String): String?
    fun getInt(key: String): Int
    fun getLong(key: String): Long
    fun getLongOptional(key: String): Long?
    fun getFloat(key: String): Float
    fun getBoolean(key: String): Boolean
    fun getBoolean(key: String, default: Boolean): Boolean
    fun getBooleanOptional(key: String): Boolean?
}
