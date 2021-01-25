package com.vickikbt.fixitapp.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vickikbt.fixitapp.data.cache.daos.BookingsDao
import com.vickikbt.fixitapp.data.cache.daos.PostsDao
import com.vickikbt.fixitapp.data.cache.daos.ReviewsDao
import com.vickikbt.fixitapp.data.cache.daos.UserDao
import com.vickikbt.fixitapp.models.entity.Booking
import com.vickikbt.fixitapp.models.entity.Post
import com.vickikbt.fixitapp.models.entity.Review
import com.vickikbt.fixitapp.models.entity.User

@Database(
    entities = [User::class, Post::class, Review::class, Booking::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDAO(): UserDao
    abstract fun postDao(): PostsDao
    abstract fun reviewDao(): ReviewsDao
    abstract fun bookingDao(): BookingsDao

}