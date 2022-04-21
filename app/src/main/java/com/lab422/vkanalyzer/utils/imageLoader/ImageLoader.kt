package com.lab422.vkanalyzer.utils.imageLoader

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import java.lang.ref.WeakReference

interface ImageLoader {
    suspend fun clearMemoryCache()

    fun loadPhotoByUrl(
        imageView: ImageView,
        url: String,
        options: RequestOptions? = null
    )
}

class ImageLoaderImpl(private val appContext: Context) : ImageLoader {

    override suspend fun clearMemoryCache() {
        Glide.get(appContext).clearDiskCache()
    }

    override fun loadPhotoByUrl(
        imageView: ImageView,
        url: String,
        options: RequestOptions?
    ) {
        val weakImageView: WeakReference<ImageView> = WeakReference(imageView)
        val requestOptions = options ?: RequestOptions.centerCropTransform()

        weakImageView.get()?.let { safeImageView ->
            Glide.with(safeImageView.context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(getShimmerAnimation())
                .skipMemoryCache(true)
                .load(Uri.parse(url))
                .apply(requestOptions)
                .into(safeImageView)
        }
    }

    private fun getShimmerAnimation(): ShimmerDrawable {
        return ShimmerDrawable().apply {
            setShimmer(
                Shimmer.AlphaHighlightBuilder()
                    .setDuration(1000)
                    .setBaseAlpha(0.9f)
                    .setHighlightAlpha(0.8f)
                    .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                    .setAutoStart(true)
                    .build()
            )
        }
    }
}

