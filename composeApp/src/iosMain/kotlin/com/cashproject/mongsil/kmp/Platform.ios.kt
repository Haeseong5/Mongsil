package com.cashproject.mongsil.kmp

import androidx.compose.runtime.Composable
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import kotlin.experimental.ExperimentalNativeApi

@Composable
actual fun getPlatformName(): String {
    return "Running on iOS 🍎"
}

actual fun getAppPlatform(): AppPlatform = AppPlatform.IOS

@OptIn(ExperimentalNativeApi::class)
actual val isDebug: Boolean = kotlin.native.Platform.isDebugBinary

actual fun openAppStore() {
    val url = NSURL.URLWithString("https://apps.apple.com/app/id6742614925")
    if (url != null) {
        UIApplication.sharedApplication.openURL(url)
    }
}
