package com.vickikbt.fixitapp.data.network

import com.vickikbt.fixitapp.models.network.AccessToken
import com.vickikbt.fixitapp.models.network.StkPush
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MpesaService {

    //Getting Access Token
    @GET("oauth/v1/generate?grant_type=client_credentials")
    suspend fun getAccessToken(): Response<AccessToken>

    //Sending phone number and amount for processing
    @POST("mpesa/stkpush/v1/processrequest")
    suspend fun stkPush(@Body stkPush: StkPush): Response<StkPush>
}