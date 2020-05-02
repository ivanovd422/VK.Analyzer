package com.lab422.vkanalyzer.ui.photosNear

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.utils.extensions.setVisible
import kotlinx.android.synthetic.main.fragment_photos_near.*
import org.koin.androidx.viewmodel.ext.android.getViewModel


class PhotosNearFragment : Fragment(R.layout.fragment_photos_near), PermissionsNeverAskDialog.OpenPermissionsSettingsAction {

    private var viewModel: PhotosNearViewModel? = null
    private var locationClientManager: FusedLocationProviderClient? = null

    companion object {
        const val TAG = "PhotosNearFragment"
        const val LOCATION_PERMISSION_ID = 10001
        fun newInstance() = PhotosNearFragment()
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
                tv_permission_needs_title.setVisible(isLocationAvailable.not())
                btn_request_permissions.setVisible(isLocationAvailable.not())
                tv_test_title.setVisible(isLocationAvailable)
            }
        })
    }

    private fun initViews() {
        btn_request_permissions.setOnClickListener {
            requestLocationPermissions()
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
                        viewModel?.onCurrentLocationReceived(
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
        } catch (e: Exception) {}
    }

    private fun showPermissionsInfoDialog() {
        PermissionsDialog().show(childFragmentManager, null)
    }

    private fun showPermissionsNeverAskDialog() {
        PermissionsNeverAskDialog().show(childFragmentManager, null)
    }

    private inner class PhotosLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            viewModel?.onCurrentLocationReceived(
                locationResult.lastLocation.latitude.toString(),
                locationResult.lastLocation.longitude.toString()
            )
        }
    }
}