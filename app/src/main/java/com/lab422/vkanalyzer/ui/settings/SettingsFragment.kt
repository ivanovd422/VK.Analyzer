package com.lab422.vkanalyzer.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.lab422.common.AppSettings
import com.lab422.vkanalyzer.BuildConfig
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.databinding.FragmentSettingsBinding
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import com.lab422.vkanalyzer.utils.navigator.Navigator
import org.koin.android.ext.android.get

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    companion object {
        const val TAG = "SettingsFragment"
        fun newInstance() = SettingsFragment()
    }

    private val trackerService: TrackerService = get()
    private val navigator: Navigator = get()
    private val appSettings: AppSettings = get()

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        with(binding) {
            tvSupport.setOnClickListener {
                sendEmail()
            }
            tvShareApp.setOnClickListener {
                shareApp()
            }

            val codeVersion = BuildConfig.VERSION_NAME
            tvAppVersionTitle.text = "${getString(R.string.settings_app_version)} $codeVersion"

            tvExitAccount.setOnClickListener {
                logout()
            }
        }
    }

    private fun shareApp() {
        val appName = activity?.applicationContext?.packageName
        val linkName = "https://play.google.com/store/apps/details?id=$appName"
        val shareText = getString(R.string.settings_share_text)
        trackerService.onShareAppClicked()

        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val shareMessage = "$shareText \n$linkName"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            context?.startActivity(shareIntent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Ошибка при попытке поделиться..", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendEmail() {
        try {
            trackerService.onSupportClicked()
            val dataIntent = Intent(
                Intent.ACTION_SENDTO,
                Uri.fromParts(
                    getString(R.string.settings_help_mailto),
                    getString(R.string.settings_support_email),
                    null
                )
            )
            context?.packageManager?.let {
                val emailApp = dataIntent.resolveActivity(it)
                if (emailApp != null) {
                    val intent = Intent.createChooser(dataIntent, getString(R.string.settings_support_email))
                    context?.startActivity(intent)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Ошибка при попытке отправить письмо..", Toast.LENGTH_SHORT).show()
        }
    }

    private fun logout() {
        appSettings.logOut()
        navigator.openLoginActivity()
    }
}
