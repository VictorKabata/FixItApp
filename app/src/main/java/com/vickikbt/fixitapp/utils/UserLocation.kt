package com.vickikbt.fixitapp.utils

import android.content.Context
import android.location.Geocoder
import java.util.*

//TODO: Move to LocationHelper
object UserLocation {

    fun getAddressName(context: Context, latitude: Double, longitude: Double): String {
        val geoCoder = Geocoder(context, Locale.getDefault())
        val address = geoCoder.getFromLocation(latitude, longitude, 1)[0]

        return if (address.locality == null) {
            address.featureName
        } else {
            address.locality
        }
    }

    fun getRegionName(context: Context, latitude: Double, longitude: Double): String {
        val geoCoder = Geocoder(context, Locale.getDefault())
        val address = geoCoder.getFromLocation(latitude, longitude, 1)[0]

        return address.adminArea
    }

    fun getCountryName(context: Context, latitude: Double, longitude: Double): String {
        val geoCoder = Geocoder(context, Locale.getDefault())
        val address = geoCoder.getFromLocation(latitude, longitude, 1)[0]

        return address.countryName
    }

}