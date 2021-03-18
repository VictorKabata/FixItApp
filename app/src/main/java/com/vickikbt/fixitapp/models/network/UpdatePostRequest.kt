package com.vickikbt.fixitapp.models.network


import com.google.gson.annotations.SerializedName

data class UpdatePostRequest(
    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("worker_id")
    val workerId: Int,

    val status: String,

    val paid: Boolean
)