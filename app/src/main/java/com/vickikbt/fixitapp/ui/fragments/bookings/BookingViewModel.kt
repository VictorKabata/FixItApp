package com.vickikbt.fixitapp.ui.fragments.bookings

import androidx.databinding.Observable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.vickikbt.fixitapp.repositories.BookingRepository
import com.vickikbt.fixitapp.utils.ApiException
import com.vickikbt.fixitapp.utils.StateListener
import java.net.UnknownHostException

class BookingViewModel @ViewModelInject constructor(private val bookingRepository: BookingRepository) :
    ViewModel(), Observable {

    var stateListener:StateListener?=null

    fun getPostBooking(postId:Int)= liveData {
        stateListener?.onLoading()

        try {
            val postBookingsResponse=bookingRepository.getPostBookings(postId)
            emit(postBookingsResponse)
            stateListener?.onSuccess("Fetched Post Bookings")
            return@liveData
        }catch (e:ApiException){
            stateListener?.onFailure("${e.message}")
            return@liveData
        }catch (e:UnknownHostException){
            stateListener?.onFailure("Ensure you have internet connection")
            return@liveData
        }catch (e:Exception){
            //stateListener?.onFailure("Error fetching post bookings")
            stateListener?.onFailure("${e.message}")
            return@liveData
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

}