package com.lab422.vkanalyzer.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.amplitude.api.Amplitude
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.utils.extensions.setVisible
import com.lab422.vkanalyzer.utils.viewState.isLoading
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
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                Amplitude.getInstance().logEvent("auth by vk success")
                viewModel.onLoginSuccess(token)
            }

            override fun onLoginFailed(errorCode: Int) {
                Amplitude.getInstance().logEvent("auth by vk failed")
                viewModel.onLoginFailed()
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
            Amplitude.getInstance().logEvent("auth by vk cancelled")
            viewModel.onLoginCancelled()
        }
    }

    private fun initObservers() {
        viewModel.getErrorState().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.getState().observe(this, Observer { viewState ->
            showLoading(viewState.isLoading())
        })
    }

    private fun showLoading(isLoading: Boolean) {
        tv_title_login.setVisible(!isLoading)
        btn_auth_by_vk.setVisible(!isLoading)
        pb_auth_loading.setVisible(isLoading)
    }

    private fun initViews() {
        btn_auth_by_vk.setOnClickListener {
            authByVk()
        }
    }

    private fun authByVk() {
        viewModel.onLoginStart()
        VK.login(this, arrayListOf(VKScope.FRIENDS, VKScope.PHOTOS))
    }
}