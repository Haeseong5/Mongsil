package com.cashproject.mongsil.kmp.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

// 테스트 광고 단위 ID — 실제 배포 시 AdMob 콘솔에서 발급받은 ID로 교체
private const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

@Composable
actual fun BannerAdView(modifier: Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = BANNER_AD_UNIT_ID
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}
