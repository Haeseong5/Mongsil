package com.cashproject.mongsil.kmp.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.cashproject.mongsil.kmp.MainActivity
import com.cashproject.mongsil.kmp.R
import com.cashproject.mongsil.kmp.core.datastore.KEY_IS_DIARY_REMINDER_ENABLED
import com.cashproject.mongsil.kmp.core.datastore.SETTINGS_PREFERENCES_NAME
import com.cashproject.mongsil.kmp.screen.setting.AndroidDiaryReminderScheduler

class DiaryReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val prefs = context.getSharedPreferences(SETTINGS_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val isEnabled = prefs.getBoolean(KEY_IS_DIARY_REMINDER_ENABLED, false)
        if (!isEnabled) return

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

        val contentIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.notification_message))
            .setContentIntent(contentIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
        AndroidDiaryReminderScheduler(context).scheduleReminder()
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            enableLights(true)
            enableVibration(true)
            lightColor = Color.WHITE
            description = CHANNEL_DESCRIPTION
        }

        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val NOTIFICATION_ID = 2008
        const val CHANNEL_ID = "diary_reminder_channel"
        private const val CHANNEL_NAME = "일기 알림"
        private const val CHANNEL_DESCRIPTION = "매일 저녁 일기 작성을 알려주는 알림"
    }
}
