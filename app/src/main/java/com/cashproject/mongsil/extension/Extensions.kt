package com.cashproject.mongsil.extension

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.os.bundleOf
import com.cashproject.mongsil.base.ScreenConfiguration
import com.google.android.play.core.review.ReviewManagerFactory
import androidx.core.net.toUri


fun openPlayStore(context: Context) {
    val packageName: String = context.packageName
    try {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$packageName")
            )
        )
    } catch (anfe: ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}

/**
 * Play Store 리뷰 작성 화면으로 직접 이동
 */
fun openPlayStoreForReview(context: Context) {
    val packageName: String = context.packageName
    try {
        // Play Store 리뷰 작성 화면으로 직접 이동 (Android 5.0 이상)
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                "market://details?id=$packageName&showAllReviews=true".toUri()
            )
        )
    } catch (anfe: ActivityNotFoundException) {
        // Play Store 앱이 없는 경우 웹 브라우저로 열기
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                "https://play.google.com/store/apps/details?id=$packageName&showAllReviews=true".toUri()
            )
        )
    }
}

/**
 * In-App Review API를 사용하여 앱 내에서 리뷰 요청
 * 사용자가 앱을 떠나지 않고 리뷰를 작성할 수 있음
 */
fun requestInAppReview(activity: Activity, onComplete: ((Boolean) -> Unit)? = null) {
    val reviewManager = ReviewManagerFactory.create(activity)

    // 리뷰 정보 요청
    val request = reviewManager.requestReviewFlow()
    request.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            // 리뷰 플로우 시작
            val reviewInfo = task.result
            val flow = reviewManager.launchReviewFlow(activity, reviewInfo)
            flow.addOnCompleteListener {
                // 리뷰 플로우 완료 (사용자가 리뷰를 작성했는지 여부와 관계없이 호출됨)
                onComplete?.invoke(true)
            }
        } else {
            // 리뷰 플로우 실행 실패 시 Play Store로 이동
            onComplete?.invoke(false)
            openPlayStoreForReview(activity)
        }
    }
}


fun Map<String, Any?>.toBundle(): Bundle = bundleOf(*this.toList().toTypedArray())


@Composable
fun isSmallWidthDevice(): Boolean {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    return screenWidth <= ScreenConfiguration.SMALL_DEVICE_WIDTH
}
