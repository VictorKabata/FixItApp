package com.vickikbt.fixitapp.utils

import java.util.concurrent.TimeUnit

object TimeKeeper {

    fun isTimeWithinInterval(timeInterval: Long, saveTime: Long, currentTime: Long): Boolean {
        val saved = TimeUnit.MILLISECONDS.toMinutes(saveTime)
        val current = TimeUnit.MILLISECONDS.toMinutes(currentTime)
        val interval = TimeUnit.HOURS.toMinutes(timeInterval)

        val difference = current - saved

        return difference > interval
    }

}