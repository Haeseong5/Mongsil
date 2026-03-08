package com.cashproject.mongsil.kmp.screen.diarywrite.component

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

// 테스트 광고 단위 ID — 실제 배포 시 AdMob 콘솔에서 발급받은 ID로 교체
private const val REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

@Composable
actual fun ShowRewardedAd(
    onRewarded: () -> Unit,
    onDismissed: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val activity = context as? Activity ?: run {
            onDismissed()
            return@LaunchedEffect
        }

        RewardedAd.load(
            context,
            REWARDED_AD_UNIT_ID,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() = onDismissed()
                        override fun onAdFailedToShowFullScreenContent(error: AdError) =
                            onDismissed()
                    }
                    ad.show(activity) { onRewarded() }
                }

                override fun onAdFailedToLoad(error: LoadAdError) = onDismissed()
            }
        )
    }
}
