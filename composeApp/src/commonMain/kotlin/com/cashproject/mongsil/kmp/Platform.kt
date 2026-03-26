package com.cashproject.mongsil.kmp

import androidx.compose.runtime.Composable

enum class AppPlatform {
    ANDROID,
    IOS,
    DESKTOP
}

@Composable
expect fun getPlatformName(): String

expect fun getAppPlatform(): AppPlatform

expect val isDebug: Boolean
