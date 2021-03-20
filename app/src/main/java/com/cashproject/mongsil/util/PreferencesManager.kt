package com.cashproject.mongsil.util

import android.content.Context
import android.content.SharedPreferences
import com.cashproject.mongsil.base.ApplicationClass
import com.cashproject.mongsil.base.ApplicationClass.Companion.prefs

const val FILENAME = "prefs"
private const val PREF_HOUR = "hour"
private const val PREF_MINUTE = "minute"
private const val PREF_EMOTICON = "minute"
private const val PREF_COMMENT = "comment"

object PreferencesManager {

    var hour: Int
        get() = prefs.getInt(PREF_HOUR, -1)
        set(value) = prefs.edit().putInt(PREF_HOUR, value).apply()

    var minute: Int
        get() = prefs.getInt(PREF_MINUTE, -1)
        set(value) = prefs.edit().putInt(PREF_MINUTE, value).apply()

    var selectedEmoticonId: Int
        get() = prefs.getInt(PREF_EMOTICON, 0)
        set(value) = prefs.edit().putInt(PREF_EMOTICON, value).apply()

    var isVisibilityComment: Boolean
        get() = prefs.getBoolean(PREF_COMMENT, false)
        set(value) = prefs.edit().putBoolean(PREF_COMMENT, value).apply()
}
