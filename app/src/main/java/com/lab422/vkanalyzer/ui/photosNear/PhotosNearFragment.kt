package com.lab422.vkanalyzer.ui.photosNear

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.lab422.common.StringProvider
import com.lab422.common.viewState.ViewState
import com.lab422.common.viewState.isError
import com.lab422.common.viewState.isLoading
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.base.RowDataModel
import com.lab422.vkanalyzer.ui.photosNear.adapter.PhotosAdapter
import com.lab422.vkanalyzer.ui.photosNear.adapter.UserPhotoRowType
import com.lab422.vkanalyzer.ui.photosNear.adapter.holder.LoadingViewHolder
import com.lab422.vkanalyzer.ui.photosNear.adapter.holder.PhotosViewHolder
import com.lab422.vkanalyzer.ui.photosNear.userInfo.UserInfoBottomSheet
import com.lab422.vkanalyzer.utils.extensions.setVisible
import kotlinx.android.synthetic.main.fragment_photos_near.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getViewModel


class PhotosNearFragment : Fragment(R.layout.fragment_photos_near),
    PermissionsNeverAskDialog.OpenPermissionsSettingsAction,
    PhotosViewHolder.Listener,
    LoadingViewHolder.Listener {

    private var viewModel: PhotosNearViewModel? = null
    private var locationClientManager: FusedLocationProviderClient? = null
    private var photosAdapter: PhotosAdapter
    private val stringProvider: StringProvider = get()

    companion object {
        const val TAG = "PhotosNearFragment"
        const val LOCATION_PERMISSION_ID = 10001
        fun newInstance() = PhotosNearFragment()
    }

    init {
        photosAdapter = PhotosAdapter(
            listOf(),
            stringProvider,
            this,
            this,
            this
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        locationClientManager = LocationServices.getFusedLocationProviderClient(requireContext())
        viewModel = getViewModel()

        initObservers()
        initViews()
    }

    override fun onResume() {
        super.onResume()
        viewModel?.onPermissionsGranted(checkPermissions())
    }

    private fun initObservers() {
        viewModel?.getLocationStateAvailability()?.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.data?.let { isLocationAvailable ->
                container_permissions.setVisible(isLocationAvailable.not())
                container_main.setVisible(isLocationAvailable)
            }
        })

        viewModel?.getUserPhotosDataState()?.observe(viewLifecycleOwner, Observer { viewState ->
            processState(viewState)
        })

        viewModel?.isCoordinatesExist()?.observe(viewLifecycleOwner, Observer { isCoordinatesExist ->
            if (isCoordinatesExist.not()) {
                if (checkPermissions()) {
                    getLastLocation()
                }
            }
        })
    }

    override fun onPhotoClicked(id: Int, lat: Double?, long: Double?) {
        showBottomSheetDialogFragment(id.toString(), lat, long)
    }

    override fun onNextLoading() {
       viewModel?.onNextPhotosLoad()
    }

    private fun processState(viewState: ViewState<List<RowDataModel<UserPhotoRowType, *>>>) {
        swipe_to_refresh_photos.isRefreshing = viewState.isLoading()

        setData(viewState.data)

        if (viewState.isError() && viewState.error.isNullOrEmpty().not()) {
            Toast.makeText(requireContext(), viewState.error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setData(data: List<RowDataModel<UserPhotoRowType, *>>?) {
        data?.let {
            photosAdapter.reload(data)
        }
    }

    private fun initViews() {
        btn_request_permissions.setOnClickListener {
            requestLocationPermissions()
        }

        rv_photos_near.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = photosAdapter
        }

        swipe_to_refresh_photos.setOnRefreshListener {
            viewModel?.onReloadClicked()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_ID &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            getLastLocation()
        } else {
            val showRationale = shouldShowRequestPermissionRationale(permissions[0])
            if (showRationale.not()) {
                showPermissionsNeverAskDialog()
            } else {
                showPermissionsInfoDialog()
            }
        }
    }

    override fun onOpenPermissionsSettingsClicked() {
        showLocationPermissionsSettings()
    }

    private fun requestLocationPermissions() {
        requestPermissions(
            arrayOf(
                permission.ACCESS_COARSE_LOCATION,
                permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_PERMISSION_ID
        )
    }

    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                locationClientManager?.lastLocation?.addOnCompleteListener { task ->
                    val location = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        viewModel?.onCoordinatesReceived(
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                    }
                }
            } else {
                showLocationPermissionsSettings()
            }
        } else {
            requestLocationPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        return context?.let {
            return ActivityCompat.checkSelfPermission(
                it,
                permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    it,
                    permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        } ?: false
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = ContextCompat.getSystemService(requireContext(), LocationManager::class.java)
        return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false || locationManager?.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        ) ?: false
    }

    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        locationClientManager = LocationServices.getFusedLocationProviderClient(requireContext())
        locationClientManager!!.requestLocationUpdates(
            mLocationRequest,
            PhotosLocationCallback(),
            Looper.myLooper()
        )
    }

    private fun showLocationPermissionsSettings() {
        try {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } catch (e: Exception) {
        }
    }

    private fun showPermissionsInfoDialog() {
        PermissionsDialog().show(childFragmentManager, null)
    }

    private fun showPermissionsNeverAskDialog() {
        PermissionsNeverAskDialog().show(childFragmentManager, null)
    }

    private fun showBottomSheetDialogFragment(userId: String, lat: Double?, long: Double?) {
        val bottomSheetFragment = UserInfoBottomSheet.newInstance(userId, lat, long)
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    private inner class PhotosLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            viewModel?.onCoordinatesReceived(
                locationResult.lastLocation.latitude.toString(),
                locationResult.lastLocation.longitude.toString()
            )
        }
    }
}