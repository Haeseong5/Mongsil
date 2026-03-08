package com.cashproject.mongsil.kmp.screen.diarywrite.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

// Desktop 광고 SDK 미연동 — 즉시 보상 지급 (stub)
@Composable
actual fun ShowRewardedAd(
    onRewarded: () -> Unit,
    onDismissed: () -> Unit,
) {
    LaunchedEffect(Unit) {
        onRewarded()
    }
}
