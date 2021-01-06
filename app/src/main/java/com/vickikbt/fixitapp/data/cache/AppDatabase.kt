package com.vickikbt.fixitapp.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vickikbt.fixitapp.data.cache.daos.UserDao
import com.vickikbt.fixitapp.models.entity.User

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDAO(): UserDao
    //abstract fun postDao(): PostDao

}