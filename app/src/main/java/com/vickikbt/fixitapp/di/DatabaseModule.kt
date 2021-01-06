package com.vickikbt.fixitapp.di

import android.app.Application
import androidx.room.Room
import com.vickikbt.fixitapp.data.cache.AppDatabase
import com.vickikbt.fixitapp.data.cache.daos.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)

@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun providesAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "fixit.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesUserDAO(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDAO()
    }

    /*@Provides
    @Singleton
    fun providesPostDAO(appDatabase: AppDatabase): PostDao {
        return appDatabase.postDao()
    }*/
}
