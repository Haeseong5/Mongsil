package com.cashproject.mongsil.ui.dialog

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.cashproject.mongsil.R
import com.cashproject.mongsil.ui.theme.regularFont
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

@Composable
fun AdMobDialog(
    nativeAd: NativeAd? = null,
    isLoading: Boolean = false,
    onReview: () -> Unit,
    onClose: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        AdMobDialogContent(
            nativeAd = nativeAd,
            isLoading = isLoading,
            onReview = onReview,
            onClose = onClose
        )
    }
}

@Composable
private fun AdMobDialogContent(
    modifier: Modifier = Modifier,
    nativeAd: NativeAd? = null,
    isLoading: Boolean = false,
    onReview: () -> Unit = {},
    onClose: () -> Unit = {},
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // AdMob 광고 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(250.dp)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator()
                }

                nativeAd != null -> {
                    AndroidView(
                        factory = { context ->
                            val inflater = LayoutInflater.from(context)
                            val adView = inflater.inflate(
                                R.layout.native_ad_layout,
                                null,
                                false
                            ) as NativeAdView

                            // 네이티브 광고 뷰 컴포넌트 연결
                            val headlineView = adView.findViewById<TextView>(R.id.ad_headline)
                            val adAdvertiser = adView.findViewById<TextView>(R.id.ad_advertiser)
                            val bodyView = adView.findViewById<TextView>(R.id.ad_body)
                            val iconView = adView.findViewById<ImageView>(R.id.ad_icon)
                            val mediaView =
                                adView.findViewById<com.google.android.gms.ads.nativead.MediaView>(
                                    R.id.ad_media
                                )
                            val callToActionView =
                                adView.findViewById<TextView>(R.id.ad_call_to_action)

                            // NativeAdView에 각 뷰 등록
                            adView.headlineView = headlineView
                            adView.bodyView = bodyView
                            adView.iconView = iconView
                            adView.mediaView = mediaView
                            adView.callToActionView = callToActionView

                            // 광고 데이터 설정
                            headlineView?.text = nativeAd.headline
                            adAdvertiser?.text = nativeAd.advertiser
                            bodyView?.text = nativeAd.body
                            callToActionView?.text = nativeAd.callToAction

                            // 아이콘 이미지 설정
                            if (nativeAd.icon == null) {
                                iconView?.visibility = android.view.View.GONE
                            } else {
                                iconView?.setImageDrawable(nativeAd.icon?.drawable)
                                iconView?.visibility = android.view.View.VISIBLE
                            }

                            // NativeAd 설정
                            adView.setNativeAd(nativeAd)

                            adView
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                else -> {
                    Text(
                        text = "광고를 불러오는 중...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .background(color = Color(0xFFf5f5f5))
                    .clickable {
                        onReview.invoke()
                    }
                    .padding(vertical = 16.dp)
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "리뷰 쓰기",
                    color = Color(0xFF888888),
                    fontFamily = regularFont
                )
            }

            Box(
                modifier = Modifier
                    .background(color = colorResource(R.color.colorYellow))
                    .clickable {
                        onClose.invoke()
                    }
                    .padding(vertical = 16.dp)
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "종료하기",
                    color = Color(0xFF6d5107),
                    fontFamily = regularFont
                )
            }
        }
    }
}

@Preview
@Composable
private fun AdMobDialogPreview() {
    AdMobDialogContent(
        isLoading = true
    )
}