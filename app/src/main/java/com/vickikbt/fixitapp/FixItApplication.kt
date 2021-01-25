package com.vickikbt.fixitapp

import android.app.Application
import com.rommansabbir.networkx.NetworkX
import com.rommansabbir.networkx.NetworkXObservingStrategy
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FixItApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NetworkX.startObserving(this, NetworkXObservingStrategy.REALTIME)
    }

}