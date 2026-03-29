package com.cashproject.mongsil.kmp.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.cashproject.mongsil.kmp.BuildConfig
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

/**
 * 광고 자동 갱신 주기(~60초) 마다 새 광고 = 새 impression = 수익 발생
 * Lifecycle 연동(pause/resume)은 정책상 필수
 * 수익을 높이려면 노출 시간과 클릭률(CTR)이 핵심이에요
 */
@Composable
actual fun BannerAdView(modifier: Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val adView = remember {
        AdView(context).apply {
            // 현재 화면 너비를 기준으로 최적 사이즈 자동 계산
            val density = resources.displayMetrics.density
            val adWidthDp = (resources.displayMetrics.widthPixels / density).toInt()
            val adSize =
                AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidthDp)
            setAdSize(adSize)
            adUnitId = BuildConfig.ADMOB_BANNER_AD_UNIT_ID
            loadAd(AdRequest.Builder().build())
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> adView.resume()
                Lifecycle.Event.ON_PAUSE -> adView.pause()
                Lifecycle.Event.ON_DESTROY -> adView.destroy()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            adView.destroy()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { adView },
    )
}
