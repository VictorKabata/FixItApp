package com.vickikbt.fixitapp.repositories

import android.util.Base64
import android.util.Log
import com.vickikbt.fixitapp.data.cache.AppDatabase
import com.vickikbt.fixitapp.data.network.DarajaService
import com.vickikbt.fixitapp.models.network.AccessToken
import com.vickikbt.fixitapp.models.network.StkPush
import com.vickikbt.fixitapp.utils.Constants
import com.vickikbt.fixitapp.utils.DataFormatter.Companion.getPassword
import com.vickikbt.fixitapp.utils.DataFormatter.Companion.getTimeStamp
import com.vickikbt.fixitapp.utils.DataFormatter.Companion.sanitizePhoneNumber
import com.vickikbt.fixitapp.utils.SafeApiRequest
import javax.inject.Inject

class MpesaRepository @Inject constructor(
    private val darajaService: DarajaService,
    private val appDatabase: AppDatabase
) :
    SafeApiRequest() {

    //private val accessTokenLiveData = MutableLiveData<String>()

    init {

    }

    suspend fun getAccessToken(): AccessToken {
        val key = "${Constants.CONSUMER_KEY}:${Constants.CONSUMER_SECRET}"
        val token = "Basic " + Base64.encodeToString(key.toByteArray(), Base64.NO_WRAP)
        //accessTokenLiveData.value = accessTokeResponse.accessToken

        return safeApiRequest { darajaService.getAccessToken(token) }
    }

    suspend fun makePayment(token:String,phoneNumber: String, amount: String) {
        val partyA = appDatabase.userDAO().getAuthenticatedUser().phoneNumber

        val stkPush = StkPush(
            Constants.BUSINESS_SHORT_CODE,
            getPassword(Constants.BUSINESS_SHORT_CODE, Constants.PASSKEY)!!,
            getTimeStamp(),
            Constants.TRANSACTION_TYPE,
            amount,
            sanitizePhoneNumber(partyA),
            Constants.PARTYB,
            sanitizePhoneNumber(phoneNumber),
            Constants.CALLBACKURL,
            "Mpesa Test",
            "Payment for repair services"
        )

        val bearerToken="Bearer $token"

        safeApiRequest { darajaService.stkPush(bearerToken, stkPush) }
    }

}
