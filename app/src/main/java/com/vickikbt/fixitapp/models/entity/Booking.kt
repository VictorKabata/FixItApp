package com.vickikbt.fixitapp.models.entity


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Bookings_Table")
data class Booking(
    @SerializedName("created_at")
    @ColumnInfo(name = "CreatedAt")
    val createdAt: String,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "ID")
    val id: Int,

    @SerializedName("post_id")
    @ColumnInfo(name = "PostId")
    val postId: Int,

    @ColumnInfo(name = "Status")
    val status: String,

    @ColumnInfo(name = "Comment")
    val comment: String,

    @ColumnInfo(name = "Bid")
    val bid: String,

    @Embedded(prefix = "user_")
    val user: User,

    @ColumnInfo(name = "UpdatedAt")
    @SerializedName("updated_at")
    val updatedAt: String,

    @ColumnInfo(name = "UserId")
    @SerializedName("user_id")
    val userId: Int
)