package com.lab422.vkanalyzer.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.lab422.common.viewState.isLoading
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.databinding.ActivityLoginBinding
import com.lab422.vkanalyzer.ui.base.BaseActivity
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import com.lab422.vkanalyzer.utils.extensions.setVisible
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import org.koin.android.ext.android.inject

class LoginActivity : BaseActivity() {

    private val viewModel: LoginViewModel by inject()
    private val tracker: TrackerService by inject()

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, LoginActivity()::class.java)
        }
    }

    private lateinit var binding: ActivityLoginBinding

    override fun getToolBarViewId(): Toolbar = binding.toolbarLogin.toolbarDefault

    override val toolbarName: Int = R.string.toolbar_text_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolBar(false)
        initViews()
        initObservers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                tracker.authByVkSuccess(token.userId)
                viewModel.onLoginSuccess(token)
            }

            override fun onLoginFailed(errorCode: Int) {
                tracker.authByVkFailed(errorCode)
                viewModel.onLoginFailed()
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
            tracker.authByVkCancelled()
            viewModel.onLoginCancelled()
        }
    }

    private fun initObservers() {
        viewModel.getErrorState().observe(
            this,
            {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        )

        viewModel.getState().observe(
            this,
            { viewState ->
                showLoading(viewState.isLoading())
            }
        )

        viewModel.getAuthInfoDialog().observe(
            this,
            {
                showAuthInfoDialog()
            }
        )
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            tvTitleLogin.setVisible(!isLoading)
            btnAuthByVk.setVisible(!isLoading)
            btnWhyShouldAuth.setVisible(!isLoading)
            pbAuthLoading.setVisible(isLoading)
        }
    }

    private fun initViews() {
        with(binding) {
            btnAuthByVk.setOnClickListener {
                authByVk()
            }
            btnWhyShouldAuth.setOnClickListener {
                showAuthInfoDialog()
            }
        }
    }

    private fun authByVk() {
        viewModel.onLoginStart()
        VK.login(this, arrayListOf(VKScope.FRIENDS, VKScope.PHOTOS, VKScope.OFFLINE))
    }

    private fun showAuthInfoDialog() {
        DialogAuthInfo().show(supportFragmentManager, null)
    }
}
