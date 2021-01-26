package com.vickikbt.fixitapp.ui.fragments.upload

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentUploadBinding
import com.vickikbt.fixitapp.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadFragment : Fragment(), StateListener {

    private lateinit var binding: FragmentUploadBinding
    private val viewModel: UploadViewModel by activityViewModels()

    private var selectedImage: Uri? = null

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    private var locationHelper: LocationHelper? = null

    private var latitude: Double? = null
    private var longitude: Double? = null
    private var address: String? = null
    private var region: String? = null
    private var country: String? = null

    private val pickerContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            selectedImage = uri

            binding.imageViewUpload.setImageURI(uri)

            binding.textViewSelectUpload.visibility = View.GONE
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upload, container, false)
        binding.viewModel = viewModel
        viewModel.stateListener = this

        setHasOptionsMenu(true)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        locationHelper = LocationHelper(requireActivity())

        binding.imageViewUpload.setOnClickListener {
            pickerContent.launch("image/*")
        }


        requestPermission()
        getMyLocation()

        return binding.root
    }

    private fun uploadPost(
        latitude: Double,
        longitude: Double,
        address: String,
        region: String,
        country: String
    ) {
        if (selectedImage == null) {
            requireActivity().applicationContext.toast("Please select image")
            return
        }

        val category = binding.spinnerCategoryUpload.selectedItem.toString()
        requireActivity().applicationContext.log(category)

        val body = ImageHelpers(requireActivity()).getImageBody(selectedImage!!)

        viewModel.uploadPostPicture(body).observe(viewLifecycleOwner, { imageUrl ->
            viewModel.uploadPost(
                category,
                imageUrl!!,
                latitude,
                longitude,
                address,
                region,
                country
            )
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.upload_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_upload -> {
                if (latitude != null && longitude != null) {
                    uploadPost(latitude!!, longitude!!, address!!, region!!, country!!)
                }
            }
        }
        return false
    }

    //Allow user to grant location permissions.
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            Constants.PERMISSION_ID
        )
    }

    private fun getMyLocation() {
        if (locationHelper!!.checkPermission()) {
            if (locationHelper!!.isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location = task.result
                    if (location == null) {
                        getNewLocation()

                    } else {
                        latitude = location.latitude
                        longitude = location.longitude

                        getLocationNames()

                        requireActivity().applicationContext.log("Latitude: ${latitude}, Longitude: $longitude")
                    }

                }
            } else {
                requireActivity().applicationContext.toast("Please enable location!")
            }

        } else {
            requestPermission()
        }
    }

    //Get latest latest location-The missing permission is checked in getLastLocation()
    @SuppressLint("MissingPermission")
    private fun getNewLocation() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    //Get latest latest location
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            val lastLocation = p0.lastLocation
            latitude = lastLocation.latitude
            longitude = lastLocation.longitude

            getLocationNames()

            requireActivity().applicationContext.log("New Latitude: ${latitude}, New Longitude: ${longitude}")
        }
    }

    //Get the address, region and country name.
    private fun getLocationNames() {
        address = UserLocation.getAddressName(requireActivity(), latitude!!, longitude!!)
        region = UserLocation.getRegionName(requireActivity(), latitude!!, longitude!!)
        country = UserLocation.getCountryName(requireActivity(), latitude!!, longitude!!)
    }

    override fun onLoading() {
        binding.progressBarUpload.show()
    }

    override fun onSuccess(message: String) {
        binding.progressBarUpload.hide()
        requireActivity().toast(message)
        findNavController().navigateUp()
    }

    override fun onFailure(message: String) {
        binding.progressBarUpload.hide()
        requireActivity().toast(message)
        requireActivity().log(message)
    }

}