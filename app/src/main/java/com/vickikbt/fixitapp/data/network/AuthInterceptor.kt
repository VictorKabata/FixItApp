package com.vickikbt.fixitapp.data.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor constructor(private val authToken: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $authToken")
            .build()

        return chain.proceed(request)
    }
}