package com.vickikbt.fixitapp.models.network


import com.google.gson.annotations.SerializedName

data class UpdatePostRequest(
    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("worker_id")
    val workerId: Int,

    val category: String,

    val description: String,

    @SerializedName("image_url")
    val imageUrl: String,

    val budget: String,

    val status: String,

    val latitude: Double,

    val longitude: Double,

    val address: String,

    val region: String,

    val country: String
)