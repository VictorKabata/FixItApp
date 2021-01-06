package com.vickikbt.fixitapp.models.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_table")
data class User(
    @ColumnInfo(name = "ID")
    val id: Int,

    @ColumnInfo(name = "Username")
    val username: String,

    @ColumnInfo(name = "Email")
    val email: String,

    @ColumnInfo(name = "PhoneNumber")
    @SerializedName("phone_number")
    val phoneNumber: String,

    @ColumnInfo(name = "ImageUrl")
    @SerializedName("image_url")
    val imageUrl: String,

    @ColumnInfo(name = "Specialisation")
    val specialisation: String,

    @ColumnInfo(name = "Latitude")
    val latitude: Double,

    @ColumnInfo(name = "Longitude")
    val longitude: Double,

    @ColumnInfo(name = "Address")
    val address: String,

    @ColumnInfo(name = "Region")
    val region: String,

    @ColumnInfo(name = "Country")
    val country: String,

    @ColumnInfo(name = "Token")
    val token: String?,

    @ColumnInfo(name = "UID")
    @PrimaryKey(autoGenerate = false)
    var uid: Int = 0
)