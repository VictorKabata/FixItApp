package com.vickikbt.fixitapp.models.entity


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "review_table")
data class Review(
    @ColumnInfo(name = "Comment")
    val comment: String,

    @ColumnInfo(name = "Created_At")
    @SerializedName("created_at")
    val createdAt: String,

    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo(name = "Rating")
    val rating: Int,

    @ColumnInfo(name = "Updated_At")
    @SerializedName("updated_at")
    val updatedAt: String,

    @Embedded(prefix = "user_")
    val user: User,

    @ColumnInfo(name = "userId")
    @SerializedName("user_id")
    val userId: Int,

    @ColumnInfo(name = "workerId")
    @SerializedName("worker_id")
    val workerId: Int
)