package com.vickikbt.fixitapp.models.network

import com.google.gson.annotations.SerializedName

data class StkPush(
    @SerializedName("BusinessShortCode")
    val businessShortCode:String,

    @SerializedName("Password")
    val password:String,

    @SerializedName("Timestamp")
    val timeStamp:String,

    @SerializedName("TransactionType")
    val transactionType:String,

    @SerializedName("Amount")
    val amount:String,

    @SerializedName("PartyA")
    val partyA:String,

    @SerializedName("PartyB")
    val partyB:String,

    @SerializedName("PhoneNumber")
    val phoneNumber:String,

    @SerializedName("CallBackURL")
    val callbackURL:String,

    @SerializedName("AccountReference")
    val accountReference:String,

    @SerializedName("TransactionDesc")
    val transactionDesc:String
)