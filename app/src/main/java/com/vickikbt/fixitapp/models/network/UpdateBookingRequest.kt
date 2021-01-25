package com.vickikbt.fixitapp.models.network


import com.google.gson.annotations.SerializedName

data class UpdateBookingRequest(
    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("status")
    val status: String
)