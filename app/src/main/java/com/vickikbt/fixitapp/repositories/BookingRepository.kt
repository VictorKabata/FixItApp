package com.vickikbt.fixitapp.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.vickikbt.fixitapp.data.cache.AppDatabase
import com.vickikbt.fixitapp.data.network.ApiService
import com.vickikbt.fixitapp.models.entity.Booking
import com.vickikbt.fixitapp.models.network.BookWorkRequest
import com.vickikbt.fixitapp.models.network.UpdateBookingRequest
import com.vickikbt.fixitapp.models.network.UpdatePostRequest
import com.vickikbt.fixitapp.utils.Constants.ACCEPT_BOOKING
import com.vickikbt.fixitapp.utils.Constants.IN_PROGRESS
import com.vickikbt.fixitapp.utils.Constants.REJECT_BOOKING
import com.vickikbt.fixitapp.utils.Coroutines
import com.vickikbt.fixitapp.utils.SafeApiRequest
import javax.inject.Inject

class BookingRepository @Inject constructor(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase,
    private val workRepository: WorkRepository
) : SafeApiRequest() {

    private val bookingsMutableLiveData = MutableLiveData<List<Booking>>()

    init {
        /*bookingsMutableLiveData.observeForever { bookings ->
            saveBookings(bookings)
        }*/
    }

    suspend fun getPostBookings(postId: Int) = safeApiRequest { apiService.getPostBooking(postId) }

    suspend fun bookWork(postId: Int, bid: String, comment: String) {
        val user = appDatabase.userDAO().getAuthenticatedUser()
        val token = "Bearer: ${user.token}"
        val userId = user.id

        val bookWorkRequestBody = BookWorkRequest(postId, userId, bid, comment)
        safeApiRequest { apiService.bookWork(token, bookWorkRequestBody) }
    }

    suspend fun acceptBooking(bookingId: Int,postId: Int, userId: Int): Booking {
        val updateBookingRequest = UpdateBookingRequest(userId, ACCEPT_BOOKING)
        val booking = safeApiRequest { apiService.updateBooking(bookingId, updateBookingRequest) }
        workRepository.createWork(postId,userId)

        updateBookedPost(booking)

        return booking
    }

    suspend fun rejectBooking(bookingId: Int, userId: Int) {
        val updateBookingRequest = UpdateBookingRequest(userId, REJECT_BOOKING)
        safeApiRequest { apiService.updateBooking(bookingId, updateBookingRequest) }
    }

    private suspend fun updateBookedPost(booking: Booking) {
        val user = appDatabase.userDAO().getAuthenticatedUser()
        val post = appDatabase.postDao().getPost(booking.postId)

        val token = "Bearer: ${user.token}"
        val updatePostRequestBody = UpdatePostRequest(
            user.id,
            booking.userId,
            post.category,
            post.description,
            post.imageUrl,
            post.budget,
            IN_PROGRESS,
            post.latitude,
            post.longitude,
            post.address,
            post.region,
            post.country
        )

        safeApiRequest { apiService.updatePost(token, booking.postId, updatePostRequestBody) }
        Log.e("VickiKbt", "Updating booked post")
    }

    private fun saveBookings(booking: List<Booking>) {
        Coroutines.io {
            appDatabase.bookingDao().saveBookings(booking)
        }
    }

}