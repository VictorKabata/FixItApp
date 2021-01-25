package com.vickikbt.fixitapp.models.network

import com.google.gson.annotations.SerializedName

data class BookWorkRequest(
    @SerializedName("post_id")
    val postId: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("bid")
    val bid: String,

    @SerializedName("comment")
    val comment: String
)