package com.lab422.vkanalyzer.ui.photosNear

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.lab422.vkanalyzer.R

class PhotosNearFragment : Fragment(R.layout.fragment_photos_near) {

    private lateinit var viewModel: PhotosNearViewModel

    companion object {
        const val TAG = "PhotosNearFragment"
        fun newInstance() = PhotosNearFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // viewModel = getViewModel()

        // initObservables()
        // initViews()

    }
}