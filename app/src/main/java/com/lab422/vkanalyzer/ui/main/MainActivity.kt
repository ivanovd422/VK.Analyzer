package com.lab422.vkanalyzer.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.utils.viewState.isError
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: MainViewModel by inject()

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity()::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        initObservers()
    }

    private fun initViews() {
        btn_find_friends.setOnClickListener {
            viewModel.onSearchClicked(
                et_first_user.text.toString(),
                et_second_user.text.toString()
            )
        }
    }

    private fun initObservers() {
        viewModel.getState().observe(this, Observer { viewState ->
            if (viewState.isError()) {
                Toast.makeText(this, viewState.error, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
