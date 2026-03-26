package com.cashproject.mongsil.kmp

import androidx.compose.runtime.Composable
import kotlin.experimental.ExperimentalNativeApi

@Composable
actual fun getPlatformName(): String {
    return "Running on iOS 🍎"
}

actual fun getAppPlatform(): AppPlatform = AppPlatform.IOS

@OptIn(ExperimentalNativeApi::class)
actual val isDebug: Boolean = kotlin.native.Platform.isDebugBinary
