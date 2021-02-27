package com.vickikbt.fixitapp.models.network


import com.google.gson.annotations.SerializedName

data class TransactionRequest(
    val amount: String,

    @SerializedName("post_id")
    val postId: Int,

    val type: String,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("work_id")
    val workId: Int,

    @SerializedName("worker_id")
    val workerId: Int
)