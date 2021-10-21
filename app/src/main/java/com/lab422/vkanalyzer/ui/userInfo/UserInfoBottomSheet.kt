package com.lab422.vkanalyzer.ui.userInfo

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lab422.common.viewState.ViewState
import com.lab422.common.viewState.isError
import com.lab422.common.viewState.isLoading
import com.lab422.common.viewState.isSuccess
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.databinding.BottomSheetUserInfoBinding
import com.lab422.vkanalyzer.ui.userInfo.model.PhotoInfoModel
import com.lab422.vkanalyzer.ui.userInfo.model.UserInfoModel
import com.lab422.vkanalyzer.utils.extensions.openLinkWithVkApp
import com.lab422.vkanalyzer.utils.extensions.setVisible
import com.lab422.vkanalyzer.utils.navigator.Navigator
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import java.util.Locale


class UserInfoBottomSheet : BottomSheetDialogFragment(), OnMapReadyCallback {

    private val navigator: Navigator = get()

    private var viewModel: UserInfoViewModel? = null
    private var googleMap: GoogleMap? = null
    private var latLng: LatLng? = null
    private var model: PhotoInfoModel? = null

    private lateinit var binding: BottomSheetUserInfoBinding

    companion object {
        private const val PHOTO_INFO_MODEL_KEY = "PHOTO_INFO_MODEL_KEY"

        fun newInstance(
            userId: String,
            lat: Double?,
            long: Double?,
            clickedPhotoUrl: String
        ): BottomSheetDialogFragment {
            val bottomSheetFragment = UserInfoBottomSheet()
            val bundle = Bundle()
            bundle.putParcelable(PHOTO_INFO_MODEL_KEY, PhotoInfoModel(userId, lat, long, clickedPhotoUrl))
            bottomSheetFragment.arguments = bundle

            return bottomSheetFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = arguments?.getParcelable(PHOTO_INFO_MODEL_KEY)!!
        viewModel = getViewModel {
            parametersOf(model)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetUserInfoBinding.inflate(inflater, container, false)
        val view = binding.root
        initViews()
        initObservers()
        return view
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        googleMap?.let {
            it.clear()
            it.mapType = GoogleMap.MAP_TYPE_NONE
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map == null) {
            return
        }
        googleMap = map
        if (latLng != null) {
            googleMap?.setLocation(latLng!!)
        }
    }

    private fun initObservers() {
        viewModel?.userInfoState?.observe(
            viewLifecycleOwner,
            { viewState ->
                fillUserInfo(viewState)
            }
        )
    }

    private fun initViews() {
        with(binding) {
            btnErrorClose.setOnClickListener { dismiss() }
            ivUserPhoto .layoutParams.height = getImageHeight()
            ivUserPhoto.requestLayout()
        }
    }

    private fun fillUserInfo(viewState: ViewState<UserInfoModel>) {
        binding.pbUserInfoLoading.setVisible(viewState.isLoading())
        binding.contentContainer.setVisible(viewState.isSuccess())

        if (viewState.isSuccess() && viewState.data != null) {
            val data = viewState.data!!

            setPhoto(data.userAvatarPhotoUrl, binding.ivUserAvatar, true)
            setPhoto(data.clickedPhotoUrl, binding.ivUserPhoto, false)
            binding.tvUserName.text = data.userName
            binding.tvUserLocation.text = getAddressText(data.lat, data.long)

            binding.llOpenInVk.setOnClickListener {
                dismiss()
                openLink(data.userId)
            }

            latLng = data.toLatLang()
            val shouldShowMap = latLng != null
            binding.containerMap.setVisible(shouldShowMap)

            if (shouldShowMap) {
                binding.mapLiteView.onCreate(null)
                binding.mapLiteView.getMapAsync(this)
            }

            binding.ivUserPhoto.setOnClickListener {
                navigator.openFullScreen(data.clickedPhotoUrl)
            }
        }

        binding.errorContainer.setVisible(viewState.isError())
    }

    private fun setPhoto(url: String, imageView: ImageView, isCropCircle: Boolean) {
        if (url.isNotEmpty()) {
            Glide.with(imageView.context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .load(Uri.parse(url))
                .apply() {
                    if (isCropCircle) {
                        apply(RequestOptions.circleCropTransform())
                    }
                }
                .into(imageView)
        }
    }

    private fun openLink(userId: String) {
        try {
            val link = "https://vk.com/id$userId"
            activity?.openLinkWithVkApp(link)
        } catch (e: Exception) {
            e
        }
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet: View =
            bottomSheetDialog.findViewById(R.id.design_bottom_sheet) ?: return
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        val layoutParams: ViewGroup.LayoutParams = bottomSheet.layoutParams
        val windowHeight = getWindowHeight()
        layoutParams.height = windowHeight
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity?)?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    private fun getImageHeight(): Int = (getWindowHeight() * 0.6).toInt()

    private fun getAddressText(lat: Double?, long: Double?): String? {
        if (lat == null || long == null) {
            return null
        }

        return try {
            val addresses = Geocoder(requireContext().applicationContext, Locale("ru")).getFromLocation(lat, long, 1)
            val firstLine = addresses[0].getAddressLine(0)
            val postCode = firstLine.split(",").last()

            firstLine.removeSuffix(postCode).removeSuffix(",")
        } catch (e: Exception) {
            null
        }
    }
}

private fun UserInfoModel.toLatLang(): LatLng? {
    if (lat == null || long == null) return null
    return LatLng(lat, long)
}

private fun GoogleMap?.setLocation(location: LatLng) {
    this?.run {
        moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13f))
        addMarker(MarkerOptions().position(location))
        mapType = GoogleMap.MAP_TYPE_NORMAL
        uiSettings.isMapToolbarEnabled = false
    }
}
