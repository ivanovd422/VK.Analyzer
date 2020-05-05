package com.lab422.vkanalyzer.ui.photoFullScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.bumptech.glide.Glide
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.base.BaseActivity
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import com.lab422.vkanalyzer.utils.extensions.gone
import com.lab422.vkanalyzer.utils.extensions.show
import kotlinx.android.synthetic.main.activity_photo_full_screen.*
import org.koin.android.ext.android.get
import java.lang.Exception

class PhotoFullScreen : BaseActivity(R.layout.activity_photo_full_screen) {

    private val trackerService: TrackerService = get()

    companion object {
        private const val photoUrlKey = "photoUrlKey"
        private const val bundleKey = "bundleKey"
        fun createIntent(context: Context, photoUrl: String): Intent {
            val bundle = Bundle()
            bundle.putString(photoUrlKey, photoUrl)
            val result = Intent(context, PhotoFullScreen()::class.java)
            result.putExtra(bundleKey, bundle)
            return result
        }
    }

    override fun getToolBarViewId(): Int = R.id.toolbar_photo_full_screen

    override val toolbarName: Int = R.string.full_screen_toolbar_text

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolBar()
        initViews()
    }

    private fun initViews() {
        btn_error.setOnClickListener { onBackPressed() }

        val url = intent?.getBundleExtra(bundleKey)?.getString(photoUrlKey)
        if (url.isNullOrEmpty()) {
            error_container.show()
            photo_view.gone()
            return
        }

        try {
            error_container.gone()
            photo_view.show()

            Glide.with(photo_view.context)
                .asBitmap()
                .load(Uri.parse(url))
                .into(photo_view)
        } catch (e: Exception) {
            trackerService.onPhotoLoadingError(e.toString() + e.message)
            error_container.show()
            photo_view.gone()
        }
    }
}