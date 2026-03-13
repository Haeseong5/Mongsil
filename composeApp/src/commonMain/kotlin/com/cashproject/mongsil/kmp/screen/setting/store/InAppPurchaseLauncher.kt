package com.cashproject.mongsil.kmp.screen.setting.store

import androidx.compose.runtime.Composable

interface InAppPurchaseLauncher {
    fun launch(productId: String)
}

@Composable
expect fun rememberInAppPurchaseLauncher(
    onPurchaseSuccess: (productId: String) -> Unit,
    onPurchaseCancelled: () -> Unit,
    onError: (message: String) -> Unit,
): InAppPurchaseLauncher
