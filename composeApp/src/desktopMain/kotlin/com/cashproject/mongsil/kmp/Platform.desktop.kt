package com.cashproject.mongsil.kmp

import androidx.compose.runtime.Composable

@Composable
actual fun getPlatformName(): String = "Running on Desktop 🖥️"

actual fun getAppPlatform(): AppPlatform = AppPlatform.DESKTOP
