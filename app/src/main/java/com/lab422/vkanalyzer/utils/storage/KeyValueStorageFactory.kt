package com.lab422.vkanalyzer.utils.storage

import android.content.SharedPreferences

class KeyValueStorageFactory {

    companion object {
        fun createStorage(sharedPreferences: SharedPreferences): KeyValueStorage =
            KeyValueStorageImpl(sharedPreferences)
    }
}
