package com.cashproject.mongsil.util

import com.cashproject.mongsil.base.ApplicationClass.Companion.prefs

const val FILENAME = "prefs"
private const val PREF_HOUR = "hour"
private const val PREF_MINUTE = "minute"
private const val PREF_EMOTICON = "emoticon"
private const val PREF_COMMENT = "comment"
private const val PREF_ALARM = "alarm"

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

    var alarm: Boolean
        get() = prefs.getBoolean(PREF_ALARM, false)
        set(value) = prefs.edit().putBoolean(PREF_ALARM, value).apply()
}
