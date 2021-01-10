package com.vickikbt.fixitapp.repositories

import com.vickikbt.fixitapp.data.cache.AppDatabase
import com.vickikbt.fixitapp.data.network.ApiService
import com.vickikbt.fixitapp.utils.SafeApiRequest
import javax.inject.Inject

class BookingRepository @Inject constructor(private val apiService: ApiService, private val appDatabase: AppDatabase):SafeApiRequest() {

    suspend fun getPostBookings(postId:Int)=safeApiRequest { apiService.getPostBooking(postId) }

}