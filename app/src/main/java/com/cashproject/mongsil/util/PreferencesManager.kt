package com.cashproject.mongsil.util

import android.util.Log
import com.cashproject.mongsil.base.App.Companion.prefs

const val FILENAME = "prefs"
private const val PREF_HOUR = "hour"
private const val PREF_MINUTE = "minute"
private const val PREF_EMOTICON = "emoticon"
private const val PREF_COMMENT = "comment"
private const val PREF_ALARM = "alarm"

object PreferencesManager {
    private val tag = "PreferencesManager"
    var hour: Int
        get() = prefs.getInt(PREF_HOUR, -1)
        set(value) = prefs.edit().putInt(PREF_HOUR, value).apply()

    var minute: Int
        get() = prefs.getInt(PREF_MINUTE, -1)
        set(value) = prefs.edit().putInt(PREF_MINUTE, value).apply()

    var selectedEmoticonId: Int = 0
        get() {
            if (field > 14) selectedEmoticonId = 0
            return prefs.getInt(PREF_EMOTICON, 0)
        }
        set(value) = prefs.edit().putInt(PREF_EMOTICON, value).apply()

    var isVisibilityComment: Boolean
        get() = prefs.getBoolean(PREF_COMMENT, false)
        set(value) = prefs.edit().putBoolean(PREF_COMMENT, value).apply()

    var isEnabledPushNotification: Boolean
        get() {
            val value = prefs.getBoolean(PREF_ALARM, true)
            Log.d(tag, "@@@ isEnabledPushNotification get() = $value")
            return value
        }
        private set(value) {
            Log.d(tag, "@@@ isEnabledPushNotification set() = $value")
            prefs.edit().putBoolean(PREF_ALARM, value).apply()
        }

    fun updateEnablePushMessage(): Boolean {
        isEnabledPushNotification = !isEnabledPushNotification
        return isEnabledPushNotification
    }
}
