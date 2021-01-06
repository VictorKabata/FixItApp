package com.vickikbt.fixitapp.data.preferences

import android.content.Context
import com.vickikbt.fixitapp.utils.Constants

class TimePreference constructor(private val context: Context) {

    private val sharedPref = context.getSharedPreferences(Constants.SHARED_PREF_NAME, 0)
    private val editor = sharedPref.edit()

    fun saveSyncTime(syncTime: Long) {
        editor.putLong(Constants.SHARED_PREF_KEY, syncTime)
        editor.commit()
    }

    val getLastSyncTime = sharedPref.getLong(Constants.SHARED_PREF_KEY, 0)

}