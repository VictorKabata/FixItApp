package com.vickikbt.fixitapp.models.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Work_Table")
data class Work(
    @ColumnInfo(name = "ID")
    @SerializedName("id")
    val id: Int,

    @ColumnInfo(name = "UserId")
    @SerializedName("user_id")
    val userId: Int,

    @ColumnInfo(name = "WorkerId")
    @SerializedName("worker_id")
    val workerId: Int,

    @ColumnInfo(name = "PostId")
    @SerializedName("post_id")
    val postId: Int,

    @Embedded(prefix = "user_")
    @SerializedName("user")
    val user: User,

    @Embedded(prefix = "worker_")
    @SerializedName("worker")
    val worker: User,

    @Embedded(prefix = "post_")
    @SerializedName("post")
    val post: Post,

    @ColumnInfo(name = "Status")
    @SerializedName("status")
    val status: String,

    @ColumnInfo(name = "CreatedAt")
    @SerializedName("created_aat")
    val createdAt: String,

    @ColumnInfo(name = "UpdatedAt")
    @SerializedName("updated_at")
    val updatedAt: String
)