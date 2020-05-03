package com.lab422.vkanalyzer.ui.photosNear.userInfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lab422.common.viewState.ViewState
import com.lab422.common.viewState.isLoading
import com.lab422.common.viewState.isSuccess
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.photosNear.userInfo.model.UserInfoModel
import com.lab422.vkanalyzer.utils.extensions.openLink
import com.lab422.vkanalyzer.utils.extensions.setVisible
import kotlinx.android.synthetic.main.bottom_sheet_user_info.view.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf


class UserInfoBottomSheet : BottomSheetDialogFragment() {

    private var viewModel: UserInfoViewModel? = null

    private lateinit var pbUserInfoLoading: ProgressBar
    private lateinit var contentContainer: View
    private lateinit var ivUserPhoto: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var btnOpenInVk: Button

    companion object {
        private const val USER_ID_KEY = "USER_ID_KEY"

        fun newInstance(userId: String): BottomSheetDialogFragment {
            val bottomSheetFragment = UserInfoBottomSheet()
            val bundle = Bundle()
            bundle.putString(USER_ID_KEY, userId)
            bottomSheetFragment.arguments = bundle

            return bottomSheetFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel {
            parametersOf(arguments?.getString(USER_ID_KEY) ?: "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.bottom_sheet_user_info, container, false)
        initViews(view)
        initObservers()
        return view
    }


    private fun initObservers() {
        viewModel?.getUserInfoState()?.observe(viewLifecycleOwner, Observer { viewState ->
            fillUserInfo(viewState)
        })
    }

    private fun initViews(view: View) {
        pbUserInfoLoading = view.pb_user_info_loading
        contentContainer = view.content_container
        ivUserPhoto = view.iv_user_photo
        tvUserName = view.tv_user_name
        btnOpenInVk = view.btn_open_in_vk
    }

    private fun fillUserInfo(viewState: ViewState<UserInfoModel>) {
        pbUserInfoLoading.setVisible(viewState.isLoading())
        contentContainer.setVisible(viewState.isLoading().not())

        if (viewState.isSuccess() && viewState.data != null) {
            val data = viewState.data!!

            setPhoto(data.userPhotoUrl)
            tvUserName.text = data.userName

            btnOpenInVk.setOnClickListener {
                val userId: String = data.userId
                val link = "https://vk.com/id$userId"
                activity?.openLink(link)
            }
        }

    }

    private fun setPhoto(url: String) {
        if (url.isNotEmpty()) {
            Glide.with(ivUserPhoto.context)
                .asBitmap()
                .load(Uri.parse(url))
                .apply(RequestOptions.circleCropTransform())
                .into(ivUserPhoto)
        }
    }
}