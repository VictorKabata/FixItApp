package com.vickikbt.fixitapp.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vickikbt.fixitapp.data.cache.daos.PostDao
import com.vickikbt.fixitapp.data.cache.daos.ReviewsDao
import com.vickikbt.fixitapp.data.cache.daos.UserDao
import com.vickikbt.fixitapp.models.entity.Post
import com.vickikbt.fixitapp.models.entity.Review
import com.vickikbt.fixitapp.models.entity.User

@Database(entities = [User::class, Post::class, Review::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDAO(): UserDao
    abstract fun postDao(): PostDao
    abstract fun reviewDao(): ReviewsDao

}