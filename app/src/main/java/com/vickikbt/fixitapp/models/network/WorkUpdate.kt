package com.vickikbt.fixitapp.models.network

import com.google.gson.annotations.SerializedName

data class WorkUpdate(
    @SerializedName("post_id")
    val postId:Int,

    @SerializedName("user_id")
    val userId:Int,

    @SerializedName("worker_id")
    val workerId:Int,

    @SerializedName("status")
    val status:String
)