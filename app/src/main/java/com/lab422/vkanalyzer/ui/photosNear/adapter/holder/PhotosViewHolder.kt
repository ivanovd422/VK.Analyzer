package com.lab422.vkanalyzer.ui.photosNear.adapter.holder

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.lab422.vkanalyzer.databinding.ItemUserPhotoRowBinding
import com.lab422.vkanalyzer.ui.base.BaseViewHolder
import com.lab422.vkanalyzer.ui.photosNear.adapter.model.UserPhotoRowModel
import com.lab422.vkanalyzer.utils.extensions.showOrHide
import com.lab422.vkanalyzer.utils.imageLoader.ImageLoader


class PhotosViewHolder(
    private val binding: ItemUserPhotoRowBinding,
    private val listener: Listener?,
    private val imageLoader: ImageLoader
) : BaseViewHolder<UserPhotoRowModel>(binding.root) {

    interface Listener {
        fun onPhotoClicked(id: Int, lat: Double?, long: Double?, clickedPhotoUrl: String)
    }

    private val cellSideSize = getCellWidth(binding.root.context)

    private companion object {
        const val COUNT_OF_COLUMNS = 3
    }

    override fun onBind(model: UserPhotoRowModel) {
        val userCells = model.userPhotosCells
        val userPhotoModel1 = userCells.firstOrNull()
        val userPhotoModel2 = if (userCells.size > 1) userCells[1] else null
        val userPhotoModel3 = if (userCells.size > 2) userCells[2] else null

        with(binding) {
            ivUserPhoto1.showOrHide(userPhotoModel1 != null)
            ivUserPhoto2.showOrHide(userPhotoModel2 != null)
            ivUserPhoto3.showOrHide(userPhotoModel3 != null)

            setViewSquareSize(ivUserPhoto1, cellSideSize)
            setViewSquareSize(ivUserPhoto2, cellSideSize)
            setViewSquareSize(ivUserPhoto3, cellSideSize)

            userPhotoModel1?.let { userModel ->
                ivUserPhoto1.setOnClickListener {
                    listener?.onPhotoClicked(
                        userModel.userId,
                        userModel.lat,
                        userModel.long,
                        userModel.photoUrl
                    )
                }
                setPhoto(ivUserPhoto1, userModel.photoUrl)
            }

            userPhotoModel2?.let { userModel ->
                ivUserPhoto2.setOnClickListener {
                    listener?.onPhotoClicked(
                        userModel.userId,
                        userModel.lat,
                        userModel.long,
                        userModel.photoUrl
                    )
                }
                setPhoto(ivUserPhoto2, userModel.photoUrl)
            }

            userPhotoModel3?.let { userModel ->
                ivUserPhoto3.setOnClickListener {
                    listener?.onPhotoClicked(
                        userModel.userId,
                        userModel.lat,
                        userModel.long,
                        userModel.photoUrl
                    )
                }
                setPhoto(ivUserPhoto3, userModel.photoUrl)
            }
        }
    }

    private fun setPhoto(imageView: ImageView, url: String) {
        imageLoader.loadPhotoByUrl(imageView, url)
    }

    private fun getCellWidth(context: Context): Int {
        val metrics = DisplayMetrics()
        val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        display.getMetrics(metrics)
        return metrics.widthPixels / COUNT_OF_COLUMNS
    }

    private fun setViewSquareSize(view: View, sideSize: Int) {
        view.layoutParams.height = sideSize
        view.layoutParams.width = sideSize
        view.requestLayout()
    }
}
