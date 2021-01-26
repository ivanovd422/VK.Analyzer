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
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
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
import com.lab422.vkanalyzer.ui.userInfo.model.PhotoInfoModel
import com.lab422.vkanalyzer.ui.userInfo.model.UserInfoModel
import com.lab422.vkanalyzer.utils.extensions.openLink
import com.lab422.vkanalyzer.utils.extensions.setVisible
import com.lab422.vkanalyzer.utils.navigator.Navigator
import kotlinx.android.synthetic.main.bottom_sheet_user_info.*
import kotlinx.android.synthetic.main.bottom_sheet_user_info.view.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import java.util.Locale


class UserInfoBottomSheet : BottomSheetDialogFragment(), OnMapReadyCallback {

    private val navigator: Navigator = get()

    private var viewModel: UserInfoViewModel? = null
    private var googleMap: GoogleMap? = null
    private var latLng: LatLng? = null

    private lateinit var pbUserInfoLoading: ProgressBar
    private lateinit var contentContainer: View
    private lateinit var ivUserAvatar: ImageView
    private lateinit var ivUserPhoto: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserLocation: TextView
    private lateinit var btnOpenInVk: View
    private lateinit var btnCloseDialog: Button
    private lateinit var mapView: MapView
    private lateinit var mapContainer: View

    private var model: PhotoInfoModel? = null

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
        val view = inflater.inflate(R.layout.bottom_sheet_user_info, container, false)
        initViews(view)
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
        viewModel?.getUserInfoState()?.observe(
            viewLifecycleOwner,
            Observer { viewState ->
                fillUserInfo(viewState)
            }
        )
    }

    private fun initViews(view: View) {
        pbUserInfoLoading = view.pb_user_info_loading
        contentContainer = view.content_container
        ivUserAvatar = view.iv_user_avatar
        ivUserPhoto = view.iv_user_photo
        tvUserName = view.tv_user_name
        tvUserLocation = view.tv_user_location
        btnOpenInVk = view.ll_open_in_vk
        btnCloseDialog = view.btn_error_close
        mapView = view.map_lite_view
        mapContainer = view.container_map

        btnCloseDialog.setOnClickListener { dismiss() }

        ivUserPhoto.layoutParams.height = getImageHeight()
        ivUserPhoto.requestLayout()
    }

    private fun fillUserInfo(viewState: ViewState<UserInfoModel>) {
        pbUserInfoLoading.setVisible(viewState.isLoading())
        contentContainer.setVisible(viewState.isSuccess())

        if (viewState.isSuccess() && viewState.data != null) {
            val data = viewState.data!!

            setPhoto(data.userAvatarPhotoUrl, ivUserAvatar, true)
            setPhoto(data.clickedPhotoUrl, ivUserPhoto, false)
            tvUserName.text = data.userName
            tvUserLocation.text = getAddressText(data.lat, data.long)

            btnOpenInVk.setOnClickListener {
                dismiss()
                openLink(data.userId)
            }

            latLng = data.toLatLang()
            val shouldShowMap = latLng != null
            mapContainer.setVisible(shouldShowMap)

            if (shouldShowMap) {
                mapView.onCreate(null)
                mapView.getMapAsync(this)
            }

            ivUserPhoto.setOnClickListener {
                navigator.openFullScreen(data.clickedPhotoUrl)
            }
        }

        error_container.setVisible(viewState.isError())
    }

    private fun setPhoto(url: String, imageView: ImageView, isCropCircle: Boolean) {
        if (url.isNotEmpty()) {
            Glide.with(imageView.context)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
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
            activity?.openLink(link)
        } catch (e: Exception) {
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

        val addresses = Geocoder(requireContext().applicationContext, Locale("ru")).getFromLocation(lat, long, 1)
        val firstLine = addresses[0].getAddressLine(0)
        val postCode = firstLine.split(",").last()

        return firstLine.removeSuffix(postCode).removeSuffix(",")
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
