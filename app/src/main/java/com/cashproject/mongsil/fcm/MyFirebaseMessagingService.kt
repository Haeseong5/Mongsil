package com.cashproject.mongsil.fcm

import android.annotation.SuppressLint
import android.util.Log
import com.cashproject.mongsil.model.FcmModel
import com.cashproject.mongsil.model.State
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.app.Notification

import androidx.core.app.NotificationCompat

import android.app.NotificationManager

import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager

import android.os.Build

import androidx.core.app.NotificationManagerCompat
import com.cashproject.mongsil.R
import com.cashproject.mongsil.ui.MainActivity


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = this.javaClass.simpleName
    private var job: Job = Job()

    companion object {
        const val TOKEN_COLLECTION = "UserToken"
        private const val CHANNEL_ID: String = "notification_channel_id"
        private const val CHANNEL_NAME: String = "notification_channel_name"
    }

    private val mUserTokenCollection =
        FirebaseFirestore.getInstance().collection(TOKEN_COLLECTION)

    private val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    /**
     * [토큰 생성 모니터링]
     * 새 토큰이 생성될 때마다 onNewToken 콜백이 호출됩니다.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        CoroutineScope(Dispatchers.IO + job).launch {
            try {
                sendRegistrationToServer(token).collect { state ->
                    when (state) {
                        is State.Success -> Log.d(TAG, "Success sendRegistrationToServer()")
                        is State.Failed -> Log.d(TAG, "Failed sendRegistrationToServer()")
                        else -> {}
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, e.stackTraceToString())
            }
        }
    }

    /**
     * 토큰이 확보되었으면 앱 서버로 전송하고 원하는 방법으로 저장할 수 있습니다.
     */
    private fun sendRegistrationToServer(token: String?) = flow<State<DocumentReference>> {
        if (token.isNullOrBlank()) {
            return@flow emit(State.failed("token is null or blank"))
        }
        val tokenRef = mUserTokenCollection.add(FcmModel(token)).await()
        emit(State.success(tokenRef))
    }.catch {
        emit(State.failed(it.stackTraceToString()))
    }.flowOn(Dispatchers.IO)

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            // Check if message contains a notification payload.
            remoteMessage.notification?.let {
                Log.d(TAG, "Message Notification Body: ${it.body}")
                sendMessage(remoteMessage)
            }
        }
    }

    @SuppressLint("WrongConstant", "UnspecifiedImmutableFlag")
    private fun sendMessage(remoteMessage: RemoteMessage) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        /**
         * https://velog.io/@haero_kim/Android-PendingIntent-%EA%B0%9C%EB%85%90-%EC%9D%B5%ED%9E%88%EA%B8%B0
         * Pending 은 '보류', '임박' 이런 뉘앙스를 갖고 있다.
         * PendingIntent 는, 가지고 있는 Intent 를 당장 수행하진 않고 특정 시점에 수행하도록 하는 특징을 갖고 있다.
         * 이 특정 시점이라 함은, 보통 해당 앱이 구동되고 있지 않을 때이다.
         */
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val notificationManager = NotificationManagerCompat.from(
            applicationContext
        )

        val builder: NotificationCompat.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                    val channel = NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    notificationManager.createNotificationChannel(channel)
                }
                NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            } else {
                NotificationCompat.Builder(applicationContext)
            }

        val title: String = remoteMessage.notification?.title ?: return
        val body: String = remoteMessage.notification?.body ?: return

        builder
            .setSmallIcon(R.drawable.emoticon_01_happy)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.emoticon_01_happy))
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSound(defaultSoundUri)
            .setShowWhen(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setVisibility(Notification.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.emoticon_01_happy)

        val notification: Notification = builder.build()
        notificationManager.notify(1, notification)
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
        Log.d(TAG, "++onDestroy()")
    }

}