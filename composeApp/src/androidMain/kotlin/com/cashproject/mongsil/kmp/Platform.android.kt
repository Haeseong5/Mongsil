package com.cashproject.mongsil.kmp

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import org.koin.java.KoinJavaComponent.getKoin

@Composable
actual fun getPlatformName(): String {
    return "Running on Android 🤖"
}

actual fun getAppPlatform(): AppPlatform = AppPlatform.ANDROID

actual val isDebug: Boolean = BuildConfig.DEBUG

actual fun openAppStore() {
    val context = getKoin().get<android.content.Context>()
    val packageName = context.packageName
    try {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                "market://details?id=$packageName".toUri()
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    } catch (e: android.content.ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                "https://play.google.com/store/apps/details?id=$packageName".toUri()
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
