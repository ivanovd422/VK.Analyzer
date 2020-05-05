package com.lab422.vkanalyzer.ui.photosNear.adapter.holder

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lab422.common.StringProvider
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.base.BaseTypedViewHolder
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.base.ViewHolderFactory
import com.lab422.vkanalyzer.ui.photosNear.adapter.UserPhotoRowType
import com.lab422.vkanalyzer.ui.photosNear.adapter.model.UserPhotoRowModel
import com.lab422.vkanalyzer.utils.extensions.show
import com.lab422.vkanalyzer.utils.extensions.showOrHide
import kotlinx.android.synthetic.main.item_user_photo_row.view.*

class PhotosViewHolder(
    view: View,
    private val listener: Listener?
) : BaseTypedViewHolder<UserPhotoRowType>(view) {

    interface Listener {
        fun onPhotoClicked(id: Int, lat: Double?, long: Double?, clickedPhotoUrl: String)
    }

    private val ivUserPhoto1 = view.iv_user_photo1
    private val ivUserPhoto2 = view.iv_user_photo2
    private val ivUserPhoto3 = view.iv_user_photo3

    companion object {
        private class Factory(private var listener: Listener?) : ViewHolderFactory {
            @Suppress("unused")
            override fun <T> createViewHolder(
                parent: ViewGroup,
                viewType: Int,
                stringProvider: StringProvider
            ): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_photo_row, parent, false)
                return PhotosViewHolder(
                    view,
                    listener
                )
            }
        }

        fun getFactory(listener: Listener? = null): ViewHolderFactory {
            return Factory(
                listener
            )
        }
    }

    override fun onBind(model: RowDataModel<UserPhotoRowType, *>) {
        super.onBind(model)
        val userCells = (model.value as UserPhotoRowModel).userPhotosCells
        val userPhotoModel1 = userCells.firstOrNull()
        val userPhotoModel2 = if (userCells.size > 1) userCells[1] else null
        val userPhotoModel3 = if (userCells.size > 2) userCells[2] else null

        ivUserPhoto1.showOrHide(userPhotoModel1 != null)
        ivUserPhoto2.showOrHide(userPhotoModel2 != null)
        ivUserPhoto3.showOrHide(userPhotoModel3 != null)

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

    private fun setPhoto(imageView: ImageView, url: String) {
        Glide.with(itemView.context)
            .asBitmap()
            .load(Uri.parse(url))
            .apply(RequestOptions.circleCropTransform())
            .into(imageView)
    }
}
