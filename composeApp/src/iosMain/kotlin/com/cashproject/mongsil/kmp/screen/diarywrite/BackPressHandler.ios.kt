package com.cashproject.mongsil.kmp.screen.diarywrite

import androidx.compose.runtime.Composable

@Composable
actual fun BackPressHandler(onBack: () -> Unit) {
    // iOS에서는 시스템 뒤로가기가 자동으로 처리되므로 별도 구현 불필요
    // 필요시 UINavigationController의 interactivePopGestureRecognizer 처리
}
