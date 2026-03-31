package com.cashproject.mongsil.kmp

import androidx.compose.runtime.Composable

@Composable
actual fun getPlatformName(): String = "Running on Desktop 🖥️"

actual fun getAppPlatform(): AppPlatform = AppPlatform.DESKTOP

actual val isDebug: Boolean = System.getProperty("debug") == "true"

actual fun openAppStore() {
    // Desktop은 스토어 업데이트 미지원
}
