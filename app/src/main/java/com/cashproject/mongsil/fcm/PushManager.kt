package com.cashproject.mongsil.fcm

import android.content.Context
import android.util.Log
import com.cashproject.mongsil.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * 푸시 메세지 동의 팝업
 * 푸시메세지 안 받기
 */

/**
 * [MyFirebaseMessagingService]
 */
class PushManager {

    private val tag = this.javaClass.name

    companion object {
        private const val PUSH_TOPIC_EVENT = "event"
        private const val PUSH_TOPIC_UPDATE = "update"

        private val regionPushNotificationEvent = MutableSharedFlow<Boolean>(replay = 0, extraBufferCapacity = 1)
        val pushNotificationEvent get() = regionPushNotificationEvent.asSharedFlow()
    }

    fun emitPushEvent(isEnabled: Boolean) {
        regionPushNotificationEvent.tryEmit(isEnabled)
    }

    fun updatePushNotificationSubscription(context: Context, isEnabled: Boolean) {
        if (isEnabled) subscribePushNotification(context)
        else unsubscribePushNotification(context)
    }

    private fun subscribePushNotification(context: Context) {
        FirebaseMessaging.getInstance().subscribeToTopic(PUSH_TOPIC_EVENT)
            .addOnCompleteListener { task ->
                val msg = context.getString(R.string.fcm_event_msg)
                if (!task.isSuccessful) {
                    Log.w(tag, "FCM event message is failed")
                }
                Log.d(tag, msg)
            }

        FirebaseMessaging.getInstance().subscribeToTopic(PUSH_TOPIC_UPDATE)
            .addOnCompleteListener { task ->
                val msg = context.getString(R.string.fcm_event_msg)
                if (!task.isSuccessful) {
                    Log.w(tag, "FCM event message is failed")
                }
                Log.d(tag, msg)
            }
    }

    private fun unsubscribePushNotification(context: Context) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(PUSH_TOPIC_EVENT)
            .addOnCompleteListener { task ->
                val msg = context.getString(R.string.fcm_event_msg)
                if (!task.isSuccessful) {
                    Log.w(tag, "FCM event message is failed")
                }
                Log.d(tag, msg)
            }

        FirebaseMessaging.getInstance().unsubscribeFromTopic(PUSH_TOPIC_UPDATE)
            .addOnCompleteListener { task ->
                val msg = context.getString(R.string.fcm_event_msg)
                if (!task.isSuccessful) {
                    Log.w(tag, "FCM event message is failed")
                }
                Log.d(tag, msg)
            }
    }

    fun loadFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(tag, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
            Log.d(tag, "Fetching FCM registration token success: $token")
        })
    }
}