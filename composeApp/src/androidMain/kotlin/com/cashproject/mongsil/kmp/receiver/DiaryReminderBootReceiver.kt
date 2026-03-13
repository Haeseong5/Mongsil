package com.cashproject.mongsil.kmp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cashproject.mongsil.kmp.core.datastore.KEY_IS_DIARY_REMINDER_ENABLED
import com.cashproject.mongsil.kmp.core.datastore.SETTINGS_PREFERENCES_NAME
import com.cashproject.mongsil.kmp.screen.setting.AndroidDiaryReminderScheduler

class DiaryReminderBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val prefs = context.getSharedPreferences(SETTINGS_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val isEnabled = prefs.getBoolean(KEY_IS_DIARY_REMINDER_ENABLED, false)
        if (!isEnabled) return

        AndroidDiaryReminderScheduler(context).scheduleReminder()
    }
}
