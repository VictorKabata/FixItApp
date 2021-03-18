package com.vickikbt.fixitapp.ui.fragments.bookings

import androidx.databinding.Observable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.vickikbt.fixitapp.repositories.BookingRepository
import com.vickikbt.fixitapp.repositories.WorkRepository
import com.vickikbt.fixitapp.utils.ApiException
import com.vickikbt.fixitapp.utils.Constants
import com.vickikbt.fixitapp.utils.Constants.INTERNET_MESSAGE
import com.vickikbt.fixitapp.utils.StateListener
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class BookingViewModel @ViewModelInject constructor(
    private val bookingRepository: BookingRepository,
    private val workRepository: WorkRepository
) : ViewModel(), Observable {

    var stateListener: StateListener? = null

    fun getPostBooking(postId: Int) = liveData {
        stateListener?.onLoading()

        try {
            val postBookingsResponse = bookingRepository.getPostBookings(postId)
            emit(postBookingsResponse)
            stateListener?.onSuccess("Fetched Post Bookings")
            return@liveData
        } catch (e: ApiException) {
            stateListener?.onFailure("${e.message}")
            return@liveData
        } catch (e: UnknownHostException) {
            stateListener?.onFailure(INTERNET_MESSAGE)
            return@liveData
        } catch (e: Exception) {
            //stateListener?.onFailure("Error fetching post bookings")
            stateListener?.onFailure("${e.message}")
            return@liveData
        }
    }

    fun acceptBooking(bookingId: Int, postId: Int, userId: Int) {
        stateListener?.onLoading()

        viewModelScope.launch {
            try {
                bookingRepository.updateBookedPost(postId, workerId = userId, Constants.STATUS_IN_PROGRESS, false)
                bookingRepository.acceptBooking(bookingId, postId, userId)
                stateListener?.onSuccess("To start soon")
                return@launch
            } catch (e: ApiException) {
                stateListener?.onFailure("${e.message}")
                return@launch
            } catch (e: UnknownHostException) {
                stateListener?.onFailure(INTERNET_MESSAGE)
                return@launch
            } catch (e: Exception) {
                stateListener?.onFailure("${e.message}")
                return@launch
            }
        }
    }

    fun rejectBooking(bookingId: Int, userId: Int) {
        stateListener?.onLoading()

        viewModelScope.launch {
            try {
                bookingRepository.rejectBooking(bookingId, userId)
                stateListener?.onSuccess("Rejected")
                return@launch
            } catch (e: ApiException) {
                stateListener?.onFailure("${e.message}")
                return@launch
            } catch (e: UnknownHostException) {
                stateListener?.onFailure(INTERNET_MESSAGE)
                return@launch
            } catch (e: Exception) {
                stateListener?.onFailure("${e.message}")
                return@launch
            }
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

}