package com.lab422.vkanalyzer.ui.loading

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lab422.vkanalyzer.R
import org.koin.android.ext.android.inject


class LoadingActivity : AppCompatActivity(R.layout.activity_loading_screen) {

    private val model: LoadingViewModel by inject()

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, LoadingActivity()::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.onCreated()
    }
}