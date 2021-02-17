package com.cashproject.mongsil.util

import android.content.Context
import android.content.SharedPreferences
import com.cashproject.mongsil.base.ApplicationClass
import com.cashproject.mongsil.base.ApplicationClass.Companion.prefs

const val FILENAME = "prefs"
private const val PREF_HOUR = "hour"
private const val PREF_MINUTE = "minute"

object PreferencesManager {

    var hour: Int
        get() = prefs.getInt(PREF_HOUR, -1)
        set(value) = prefs.edit().putInt(PREF_HOUR, value).apply()

    var minute: Int
        get() = prefs.getInt(PREF_MINUTE, -1)
        set(value) = prefs.edit().putInt(PREF_MINUTE, value).apply()
}
