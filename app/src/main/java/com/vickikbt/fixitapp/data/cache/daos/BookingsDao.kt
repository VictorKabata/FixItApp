package com.vickikbt.fixitapp.data.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vickikbt.fixitapp.models.entity.Booking
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingsDao {

    @Insert
    suspend fun saveBookings(booking: List<Booking>)

    @Query("SELECT * FROM Bookings_Table")
    fun getAllBookings(): Flow<List<Booking>>

    @Query("SELECT * FROM Bookings_Table ")
    fun getAllBooking(): Flow<Booking>

}