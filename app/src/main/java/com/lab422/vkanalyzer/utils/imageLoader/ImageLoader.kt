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

class ImageLoaderImpl(private val context: Context) : ImageLoader {

    private val shimmer = Shimmer.AlphaHighlightBuilder()
        .setDuration(1800)
        .setBaseAlpha(0.7f)
        .setHighlightAlpha(0.6f)
        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
        .setAutoStart(true)
        .build()

    private val shimmerDrawable = ShimmerDrawable().apply {
        setShimmer(shimmer)
    }

    override suspend fun clearMemoryCache() {
        Glide.get(context).clearDiskCache()
    }

    override fun loadPhotoByUrl(
        imageView: ImageView,
        url: String,
        options: RequestOptions?
    ) {
        val weakImageView: WeakReference<ImageView> = WeakReference(imageView)
        val requestOptions = options ?: RequestOptions.centerCropTransform()

        weakImageView.get()?.let {
            Glide.with(context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(shimmerDrawable)
                .skipMemoryCache(true)
                .load(Uri.parse(url))
                .apply(requestOptions)
                .into(imageView)
        }
    }
}

