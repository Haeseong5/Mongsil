package com.cashproject.mongsil.kmp.screen.setting

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSDateComponents
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.coroutines.resume

class IosDiaryReminderScheduler : DiaryReminderScheduler {

    override suspend fun setEnabled(enabled: Boolean): Boolean {
        val center = UNUserNotificationCenter.currentNotificationCenter()

        if (!enabled) {
            center.removePendingNotificationRequestsWithIdentifiers(listOf(REQUEST_ID))
            center.removeDeliveredNotificationsWithIdentifiers(listOf(REQUEST_ID))
            return false
        }

        val granted = requestAuthorization(center)
        if (!granted) {
            center.removePendingNotificationRequestsWithIdentifiers(listOf(REQUEST_ID))
            return false
        }

        center.removePendingNotificationRequestsWithIdentifiers(listOf(REQUEST_ID))

        val content = UNMutableNotificationContent().apply {
            setTitle("몽실")
            setBody("오늘 하루는 어땠나요?")
            setSound(UNNotificationSound.defaultSound)
        }

        val components = NSDateComponents().apply {
            hour = 20
            minute = 0
        }

        val trigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
            dateComponents = components,
            repeats = true,
        )
        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = REQUEST_ID,
            content = content,
            trigger = trigger,
        )

        return suspendCancellableCoroutine { continuation ->
            center.addNotificationRequest(request) { error ->
                continuation.resume(error == null)
            }
        }
    }

    private suspend fun requestAuthorization(center: UNUserNotificationCenter): Boolean =
        suspendCancellableCoroutine { continuation ->
            center.requestAuthorizationWithOptions(
                options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge,
            ) { granted, _ ->
                continuation.resume(granted)
            }
        }

    companion object {
        private const val REQUEST_ID = "daily_diary_reminder"
    }
}
