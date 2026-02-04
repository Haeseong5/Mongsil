package com.cashproject.mongsil.kmp.screen.diarywrite

import androidx.compose.runtime.Composable

/**
 * 플랫폼별 뒤로가기 처리를 위한 expect 함수
 * Android: BackHandler 사용
 * iOS: 네비게이션 스택 관리
 */
@Composable
expect fun BackPressHandler(enabled: Boolean = true, onBack: () -> Unit)
