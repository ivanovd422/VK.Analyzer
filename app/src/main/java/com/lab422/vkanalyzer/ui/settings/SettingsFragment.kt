package com.lab422.vkanalyzer.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.lab422.vkanalyzer.R
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    // private lateinit var viewModel: SettingsViewModel

    companion object {
        const val TAG = "SettingsFragment"
        fun newInstance() = SettingsFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // viewModel = getViewModel()

        // initObservables()
        // initViews()

    }
}