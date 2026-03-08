package com.cashproject.mongsil.kmp.screen.diarywrite.component

import androidx.compose.runtime.Composable

/**
 * 플랫폼별 영상 광고를 재생하는 컴포저블.
 * 컴포지션에 진입하는 순간 광고를 로드·재생합니다.
 *
 * @param onRewarded 광고를 끝까지 시청한 경우 호출
 * @param onDismissed 광고를 닫거나 로드에 실패한 경우 호출
 */
@Composable
expect fun ShowRewardedAd(
    onRewarded: () -> Unit,
    onDismissed: () -> Unit,
)
