package com.vickikbt.fixitapp.models.network

import com.google.gson.annotations.SerializedName

data class ReviewUserRequest(
    @SerializedName("user_id")
    val userId:Int,

    @SerializedName("worker_id")
    val workerId:Int,

    @SerializedName("comment")
    val comment:String,

    @SerializedName("rating")
    val rating:Int
)