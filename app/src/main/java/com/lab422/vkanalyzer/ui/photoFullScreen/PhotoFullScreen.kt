package com.lab422.vkanalyzer.ui.photoFullScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.databinding.ActivityPhotoFullScreenBinding
import com.lab422.vkanalyzer.ui.base.BaseActivity
import com.lab422.vkanalyzer.utils.analytics.TrackerService
import com.lab422.vkanalyzer.utils.extensions.gone
import com.lab422.vkanalyzer.utils.extensions.show
import java.lang.Exception
import org.koin.android.ext.android.get

class PhotoFullScreen : BaseActivity() {

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

    private lateinit var binding: ActivityPhotoFullScreenBinding

    override fun getToolBarViewId(): Toolbar = binding.toolbarPhotoFullScreen.toolbarDefault

    override val toolbarName: Int = R.string.full_screen_toolbar_text

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoFullScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolBar()
        initViews()
    }

    private fun initViews() {
        with(binding) {
            btnError.setOnClickListener { onBackPressed() }

            val url = intent?.getBundleExtra(bundleKey)?.getString(photoUrlKey)
            if (url.isNullOrEmpty()) {
                errorContainer.show()
                photoView.gone()
                return
            }

            try {
                errorContainer.gone()
                photoView.show()

                Glide.with(photoView.context)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .load(Uri.parse(url))
                    .into(photoView)
            } catch (e: Exception) {
                trackerService.onPhotoLoadingError(e.toString() + e.message)
                errorContainer.show()
                photoView.gone()
            }
        }
    }
}
