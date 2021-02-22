package com.vickikbt.fixitapp.data.network

import android.util.Base64
import com.vickikbt.fixitapp.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response

class AccessTokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val key = "${Constants.CONSUMER_KEY} : ${Constants.CONSUMER_SECRET}"

        val request = chain.request().newBuilder()
            .addHeader(
                "Authorization",
                "Basic " + Base64.encodeToString(key.toByteArray(), Base64.NO_WRAP)
            )
            .build()

        return chain.proceed(request)
    }
}