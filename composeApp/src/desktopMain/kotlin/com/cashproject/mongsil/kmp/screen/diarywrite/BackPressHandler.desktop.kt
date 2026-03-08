package com.cashproject.mongsil.kmp.screen.diarywrite

import androidx.compose.runtime.Composable

@Composable
actual fun BackPressHandler(enabled: Boolean, onBack: () -> Unit) {
    // Desktop은 OS 수준의 뒤로가기가 없으므로 no-op
}
