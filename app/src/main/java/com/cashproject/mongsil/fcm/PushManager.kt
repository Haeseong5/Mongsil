package com.cashproject.mongsil.fcm

import android.content.Context
import android.util.Log
import com.cashproject.mongsil.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class PushManager {

    private val TAG = this.javaClass.name

    fun loadFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
            Log.d(TAG, "Fetching FCM registration token success: $token")
        })
    }

    fun subscribeEventNotification(context: Context) {
        FirebaseMessaging.getInstance().subscribeToTopic("event")
            .addOnCompleteListener { task ->
                val msg = context.getString(R.string.fcm_event_msg)
                if (!task.isSuccessful) {
                    Log.w(TAG, "FCM event message is failed")
                }
                Log.d(TAG, msg)
            }
    }

    fun subscribeUpdateNotification(context: Context) {
        FirebaseMessaging.getInstance().subscribeToTopic("update")
            .addOnCompleteListener { task ->
                val msg = context.getString(R.string.fcm_event_msg)
                if (!task.isSuccessful) {
                    Log.w(TAG, "FCM event message is failed")
                }
                Log.d(TAG, msg)
            }
    }
}