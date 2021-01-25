package com.vickikbt.fixitapp.ui.fragments.auth

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.vickikbt.fixitapp.R
import com.vickikbt.fixitapp.databinding.FragmentRegisterBinding
import com.vickikbt.fixitapp.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(), StateListener {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<UserViewModel>()

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

            binding.imageViewProfile.setImageURI(uri)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.viewModel = viewModel
        viewModel.stateListener = this

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        locationHelper = LocationHelper(requireActivity())

        binding.buttonRegistration.setOnClickListener {
            if (latitude != null && longitude != null) {
                registerUser(latitude!!, longitude!!, address!!, region!!, country!!)
            } else {
                getMyLocation()
            }
        }

        binding.imageViewProfile.setOnClickListener {
            pickerContent.launch("image/*")
        }

        binding.linearlayoutRegister.setOnClickListener { findNavController().navigateUp() }

        requestPermission()
        getMyLocation()

        return binding.root
    }

    //Register user
    private fun registerUser(
        latitude: Double,
        longitude: Double,
        address: String,
        region: String,
        country: String
    ) {
        if (selectedImage == null) {
            requireActivity().toast("Please select your profile picture")
            return
        }

        val category = binding.spinnerSpecialisation.selectedItem.toString()
        if (binding.spinnerSpecialisation.selectedItemPosition == 0) {
            requireActivity().toast("Please select your specialisation")
            return
        }

        val body = ImageHelpers(requireActivity()).getImageBody(selectedImage!!)

        viewModel.uploadProfilePic(body).observe(viewLifecycleOwner, Observer { imageUrl ->
            viewModel.registerUser(
                imageUrl!!,
                category,
                latitude,
                longitude,
                address,
                region,
                country
            )
        })
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

    //Get last location
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

                        requireActivity().log("Latitude: ${latitude}, Longitude: $longitude")
                    }

                }
            } else {
                requireActivity().toast("Please enable location!")
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

            requireActivity().log("New Latitude: ${latitude}, New Longitude: $longitude")
        }
    }

    private fun getLocationNames() {
        if (isAdded){
            address = UserLocation.getAddressName(requireActivity(), latitude!!, longitude!!)
            region = UserLocation.getRegionName(requireActivity(), latitude!!, longitude!!)
            country = UserLocation.getCountryName(requireActivity(), latitude!!, longitude!!)
        }
    }

    override fun onLoading() {
        binding.progressBarRegister.show()
    }

    override fun onSuccess(message: String) {
        binding.progressBarRegister.hide()
        requireActivity().toast(message)

        findNavController().navigate(R.id.register_to_home)
    }

    override fun onFailure(message: String) {
        binding.progressBarRegister.hide()
        requireActivity().toast(message)
    }

}