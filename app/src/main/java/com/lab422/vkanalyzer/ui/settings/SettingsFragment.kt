package com.lab422.vkanalyzer.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.lab422.vkanalyzer.BuildConfig
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.ext.android.get

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    companion object {
        const val TAG = "SettingsFragment"
        fun newInstance() = SettingsFragment()
    }

    private val trackerService: TrackerService = get()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        tv_support.setOnClickListener {
            sendEmail()
        }
        tv_share_app.setOnClickListener {
            shareApp()
        }

        val codeVersion = BuildConfig.VERSION_NAME
        tv_app_version_title.text = "${getString(R.string.settings_app_version)} $codeVersion"
    }

    private fun shareApp() {
        val appName = activity?.applicationContext?.packageName
        val linkName = "https://play.google.com/store/apps/details?id=$appName"
        val shareText = getString(R.string.settings_share_text)
        trackerService.onShareAppClicked()

        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val shareMessage = "$shareText \n${linkName}"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            context?.startActivity(shareIntent)
        } catch (e: Exception) {
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
        }
    }
}