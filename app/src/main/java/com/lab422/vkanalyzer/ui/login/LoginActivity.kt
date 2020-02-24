package com.lab422.vkanalyzer.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.lab422.vkanalyzer.R
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.inject


class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    private val viewModel: LoginViewModel by inject()

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, LoginActivity()::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initObservers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                viewModel.onLoginSuccess(token)
            }

            override fun onLoginFailed(errorCode: Int) {
                viewModel.onLoginFailed()
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
            viewModel.onLoginCancelled()
        }
    }

    private fun initObservers() {
        viewModel.getErrorState().observe(this, Observer {
           Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun initViews() {
        btn_auth_by_vk.setOnClickListener {
            authByVk()
        }
    }

    private fun authByVk() {
        VK.login(this, arrayListOf(VKScope.FRIENDS))
    }
}