package com.vickikbt.fixitapp.models.network

import com.google.gson.annotations.SerializedName
import com.vickikbt.fixitapp.models.entity.Post
import com.vickikbt.fixitapp.models.entity.User
import com.vickikbt.fixitapp.models.entity.Work

//@Entity(tableName = "transactions_table")
data class TransactionResponse(
    //@PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("worker_id")
    val workerId: Int,

    @SerializedName("post_id")
    val postId: Int,

    @SerializedName("work_id")
    val workId: Int,

    @SerializedName("amount")
    val amount: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("user")
    val user: User,

    @SerializedName("worker")
    val worker: User,

    @SerializedName("post")
    val post: Post,

    @SerializedName("work")
    val work: Work,

    @SerializedName("created_at")
    val createdAt: String
)