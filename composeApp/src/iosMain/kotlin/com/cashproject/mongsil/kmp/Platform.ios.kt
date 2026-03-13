package com.cashproject.mongsil.kmp

import androidx.compose.runtime.Composable

@Composable
actual fun getPlatformName(): String {
    return "Running on iOS 🍎"
}

actual fun getAppPlatform(): AppPlatform = AppPlatform.IOS
