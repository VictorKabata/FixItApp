package com.vickikbt.fixitapp.models.entity


import com.google.gson.annotations.SerializedName
import com.vickikbt.fixitapp.models.entity.User

//TODO: Convert to entity class

data class Booking(
    @SerializedName("created_at")
    val createdAt: String,
    val id: Int,
    @SerializedName("post_id")
    val postId: Int,
    val status: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val user: User,
    @SerializedName("user_id")
    val userId: Int
)