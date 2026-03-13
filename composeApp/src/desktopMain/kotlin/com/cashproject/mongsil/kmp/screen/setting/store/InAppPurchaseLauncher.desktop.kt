package com.cashproject.mongsil.kmp.screen.setting.store

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberInAppPurchaseLauncher(
    onPurchaseSuccess: (productId: String) -> Unit,
    onPurchaseCancelled: () -> Unit,
    onError: (message: String) -> Unit,
): InAppPurchaseLauncher {
    return remember(onError) {
        object : InAppPurchaseLauncher {
            override fun launch(productId: String) {
                onError("인앱 결제는 Android에서만 지원됩니다")
            }
        }
    }
}
