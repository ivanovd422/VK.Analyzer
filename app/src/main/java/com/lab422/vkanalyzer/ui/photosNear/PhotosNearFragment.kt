package com.lab422.vkanalyzer.ui.photosNear

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.lab422.common.viewState.ViewState
import com.lab422.common.viewState.isError
import com.lab422.common.viewState.isLoading
import com.lab422.vkanalyzer.databinding.FragmentPhotosNearBinding
import com.lab422.vkanalyzer.ui.photosNear.adapter.PhotosAdapter
import com.lab422.vkanalyzer.ui.photosNear.adapter.holder.LoadingViewHolder
import com.lab422.vkanalyzer.ui.photosNear.adapter.holder.PhotosViewHolder
import com.lab422.vkanalyzer.ui.userInfo.UserInfoBottomSheet
import com.lab422.vkanalyzer.utils.extensions.setVisible
import com.lab422.vkanalyzer.utils.imageLoader.ImageLoader
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getViewModel

class PhotosNearFragment :
    Fragment(),
    PermissionsNeverAskDialog.OpenPermissionsSettingsAction,
    PhotosViewHolder.Listener,
    LoadingViewHolder.Listener {

    private var viewModel: PhotosNearViewModel? = null
    private var locationClientManager: FusedLocationProviderClient? = null
    private var photosAdapter: PhotosAdapter? = null
    private val imageLoader: ImageLoader = get()

    private lateinit var binding: FragmentPhotosNearBinding


    companion object {
        const val TAG = "PhotosNearFragment"
        const val LOCATION_PERMISSION_ID = 10001
        fun newInstance() = PhotosNearFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotosNearBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { locationClientManager = LocationServices.getFusedLocationProviderClient(it) }
        viewModel = getViewModel()
        initViews()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel?.onPermissionsGranted(checkPermissions())
    }

    private fun initObservers() {
        viewModel?.locationStateAvailability?.observe(
            viewLifecycleOwner
        ) { viewState ->
            viewState.data?.let { isLocationAvailable ->
                binding.containerPermissions.setVisible(isLocationAvailable.not())
                binding.containerMain.setVisible(isLocationAvailable)
            }
        }

        viewModel?.userPhotosData?.observe(
            viewLifecycleOwner
        ) { viewState ->
            processState(viewState)
        }

        viewModel?.coordinatesState?.observe(
            viewLifecycleOwner
        ) { isCoordinatesExist ->
            if (isCoordinatesExist.not()) {
                if (checkPermissions()) {
                    getLastLocation()
                }
            }
        }
    }

    override fun onPhotoClicked(id: Int, lat: Double?, long: Double?, clickedPhotoUrl: String) {
        showBottomSheetDialogFragment(id.toString(), lat, long, clickedPhotoUrl)
    }

    override fun onNextLoading() {
        viewModel?.onNextPhotosLoad()
    }

    private fun processState(viewState: ViewState<List<Any>>) {
        binding.swipeToRefreshPhotos.isRefreshing = viewState.isLoading()

        setData(viewState.data)

        if (viewState.isError() && viewState.error.isNullOrEmpty().not()) {
            Toast.makeText(requireContext(), viewState.error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setData(data: List<Any>?) {
        data?.let {
            photosAdapter?.submitList(data)
        }
    }

    private fun initViews() {
        photosAdapter = PhotosAdapter(
            listener = this,
            onNextLoadingListener = this,
            imageLoader = imageLoader
        )

        with(binding) {
            btnRequestPermissions.setOnClickListener {
                requestLocationPermissions()
            }

            rvPhotosNear.run {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = photosAdapter
            }

            swipeToRefreshPhotos.setOnRefreshListener {
                viewModel?.onReloadClicked()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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

    private fun checkPermissions(): Boolean =
        context?.let {
            return ActivityCompat.checkSelfPermission(
                it,
                permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    it,
                    permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        } ?: false

    private fun isLocationEnabled(): Boolean {
        return context?.let {
            val locationManager = ContextCompat.getSystemService(it, LocationManager::class.java)
            return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false ||
                locationManager?.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER
                ) ?: false
        } ?: false
    }

    private fun requestNewLocationData() {
        context?.let {
            val mLocationRequest = LocationRequest()
            mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            mLocationRequest.interval = 0
            mLocationRequest.fastestInterval = 0
            mLocationRequest.numUpdates = 1
            locationClientManager = LocationServices.getFusedLocationProviderClient(it)
            locationClientManager!!.requestLocationUpdates(
                mLocationRequest,
                PhotosLocationCallback(),
                Looper.myLooper()
            )
        }
    }

    private fun showLocationPermissionsSettings() {
        try {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Ошибка при попытке открыть настройки..", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPermissionsInfoDialog() {
        PermissionsDialog().show(childFragmentManager, null)
    }

    private fun showPermissionsNeverAskDialog() {
        PermissionsNeverAskDialog().show(childFragmentManager, null)
    }

    private fun showBottomSheetDialogFragment(
        userId: String,
        lat: Double?,
        long: Double?,
        clickedPhotoUrl: String
    ) {
        val bottomSheetFragment = UserInfoBottomSheet.newInstance(userId, lat, long, clickedPhotoUrl)
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
