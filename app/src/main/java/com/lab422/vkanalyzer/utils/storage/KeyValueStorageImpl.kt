package com.lab422.vkanalyzer.utils.storage

import android.content.SharedPreferences



internal class KeyValueStorageImpl(private val sharedPreferences: SharedPreferences): KeyValueStorage {

    override fun <T> set(key: String, value: T) {
        when (value) {
            is String -> sharedPreferences.edit().putString(key, value).apply()
            is Int -> sharedPreferences.edit().putInt(key, value).apply()
            is Boolean -> sharedPreferences.edit().putBoolean(key, value).apply()
            is Float -> sharedPreferences.edit().putFloat(key, value).apply()
            is Long -> sharedPreferences.edit().putLong(key, value).apply()
            else -> {
            }
        }
    }

    override fun getString(key: String): String? = sharedPreferences.getString(key, null)
    override fun getInt(key: String): Int = sharedPreferences.getInt(key, 0)
    override fun getLong(key: String): Long = sharedPreferences.getLong(key, 0L)
    override fun getLongOptional(key: String): Long? =
        if (sharedPreferences.contains(key)) sharedPreferences.getLong(key, 0L) else null
    override fun getFloat(key: String): Float = sharedPreferences.getFloat(key, 0F)
    override fun getBoolean(key: String): Boolean = getBoolean(key, false)
    override fun getBoolean(key: String, default: Boolean): Boolean = sharedPreferences.getBoolean(key, default)
    override fun getBooleanOptional(key: String): Boolean? =
        if (sharedPreferences.contains(key)) sharedPreferences.getBoolean(key, false) else null
}