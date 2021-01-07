package com.vickikbt.fixitapp.data.preferences

import android.content.Context
import com.vickikbt.fixitapp.utils.Constants

class TimePreference constructor(private val context: Context) {

    private val sharedPref = context.getSharedPreferences(Constants.SHARED_PREF_NAME, 0)
    private val editor = sharedPref.edit()

    fun savePostSyncTime(syncTime: Long) {
        editor.putLong(Constants.POST_SHARED_PREF_KEY, syncTime)
        editor.commit()
    }

    val getLastPostSyncTime = sharedPref.getLong(Constants.POST_SHARED_PREF_KEY, 0)

    fun saveReviewSyncTime(syncTime: Long) {
        editor.putLong(Constants.REVIEW_SHARED_PREF_KEY, syncTime)
        editor.commit()
    }

    val getLastReviewSyncTime = sharedPref.getLong(Constants.REVIEW_SHARED_PREF_KEY, 0)

}