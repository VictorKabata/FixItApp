package com.vickikbt.fixitapp.di

import android.app.Application
import com.vickikbt.fixitapp.data.preferences.TimePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)

@Module
object PreferencesModule {

    @Provides
    fun providesTimePreference(application: Application): TimePreference {
        return TimePreference(application)
    }
}