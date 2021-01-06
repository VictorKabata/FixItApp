package com.vickikbt.fixitapp.models.network


import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    val username: String,

    val email: String,

    @SerializedName("phone_number")
    val phoneNumber: String,

    @SerializedName("image_url")
    val imageUrl: String,

    val specialisation: String,

    val password: String,

    val latitude: Double,

    val longitude: Double,

    val region: String,

    val address: String,

    val country: String
)