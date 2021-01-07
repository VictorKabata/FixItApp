package com.vickikbt.fixitapp.models.entity


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "post_table")
data class Post(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "ID")
    val id: Int,

    @SerializedName("user_id")
    //@ColumnInfo(name = "User_ID")
    val userId: Int,

    @SerializedName("worker_id")
    @ColumnInfo(name = "Worker_ID")
    val workerId: Int,

    @ColumnInfo(name = "Description")
    val description: String,

    @ColumnInfo(name = "Category")
    val category: String,

    @SerializedName("image_url")
    @ColumnInfo(name = "Post_Image_URL")
    val imageUrl: String,

    @ColumnInfo(name = "Latitude")
    val latitude: Double,

    @ColumnInfo(name = "Longitude")
    val longitude: Double,

    @ColumnInfo(name = "Budget")
    val budget: String,

    @ColumnInfo(name = "Status")
    val status: String,

    @ColumnInfo(name = "Address")
    val address: String,

    @ColumnInfo(name = "Region")
    val region: String,

    @ColumnInfo(name = "Country")
    val country: String,

    @Embedded(prefix = "user_")
    val user: User,

    @SerializedName("updated_at")
    @ColumnInfo(name = "Updated_At")
    val updatedAt: String,

    @SerializedName("created_at")
    @ColumnInfo(name = "Created_At")
    val createdAt: String
)