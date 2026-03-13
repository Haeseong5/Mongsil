package com.cashproject.mongsil.kmp.screen.setting.store

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams

private class AndroidInAppPurchaseLauncher(
    private val activityProvider: () -> Activity?,
    private val onPurchaseSuccess: (String) -> Unit,
    private val onPurchaseCancelled: () -> Unit,
    private val onError: (String) -> Unit,
) : InAppPurchaseLauncher {

    private var billingClient: BillingClient? = null
    private var isConnected = false
    private var pendingProductId: String? = null

    private val purchasesUpdatedListener = PurchasesUpdatedListener { result, purchases ->
        when (result.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                val purchaseList = purchases.orEmpty()
                if (purchaseList.isEmpty()) {
                    onError("결제 정보를 확인할 수 없습니다")
                } else {
                    purchaseList.forEach(::handlePurchase)
                }
            }

            BillingClient.BillingResponseCode.USER_CANCELED -> onPurchaseCancelled()
            else -> onError(result.debugMessage.ifBlank { "결제를 진행할 수 없습니다" })
        }
    }

    fun attach(activity: Activity?) {
        if (activity == null || billingClient != null) return

        billingClient = BillingClient.newBuilder(activity)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .build(),
            )
            .build()
            .also(::startConnection)
    }

    fun dispose() {
        billingClient?.endConnection()
        billingClient = null
        isConnected = false
        pendingProductId = null
    }

    override fun launch(productId: String) {
        pendingProductId = productId
        val activity = activityProvider()
        val client = billingClient

        if (activity == null || client == null) {
            onError("결제 화면을 열 수 없습니다")
            return
        }

        ensureConnected(client) {
            queryProductDetails(
                client = client,
                activity = activity,
                productId = productId,
            )
        }
    }

    private fun startConnection(client: BillingClient) {
        client.startConnection(
            object : BillingClientStateListener {
                override fun onBillingServiceDisconnected() {
                    isConnected = false
                }

                override fun onBillingSetupFinished(result: BillingResult) {
                    isConnected = result.responseCode == BillingClient.BillingResponseCode.OK
                    if (!isConnected) {
                        onError(result.debugMessage.ifBlank { "결제 연결에 실패했습니다" })
                    }
                }
            },
        )
    }

    private fun ensureConnected(
        client: BillingClient,
        onReady: () -> Unit,
    ) {
        if (isConnected) {
            onReady()
            return
        }

        client.startConnection(
            object : BillingClientStateListener {
                override fun onBillingServiceDisconnected() {
                    isConnected = false
                    onError("결제 연결이 끊어졌습니다")
                }

                override fun onBillingSetupFinished(result: BillingResult) {
                    isConnected = result.responseCode == BillingClient.BillingResponseCode.OK
                    if (isConnected) {
                        onReady()
                    } else {
                        onError(result.debugMessage.ifBlank { "결제 연결에 실패했습니다" })
                    }
                }
            },
        )
    }

    private fun queryProductDetails(
        client: BillingClient,
        activity: Activity,
        productId: String,
    ) {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(productId)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build(),
                ),
            )
            .build()

        client.queryProductDetailsAsync(params) { result, productDetailsList ->
            if (result.responseCode != BillingClient.BillingResponseCode.OK) {
                onError(result.debugMessage.ifBlank { "상품 정보를 불러오지 못했습니다" })
                return@queryProductDetailsAsync
            }

            val productDetails = productDetailsList.firstOrNull()
            if (productDetails == null) {
                onError("Play Console 상품 ID를 확인해주세요: $productId")
                return@queryProductDetailsAsync
            }

            launchBillingFlow(client, activity, productDetails)
        }
    }

    private fun launchBillingFlow(
        client: BillingClient,
        activity: Activity,
        productDetails: ProductDetails,
    ) {
        val params = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .build()

        val billingResult = client.launchBillingFlow(
            activity,
            BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(params))
                .build(),
        )

        if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
            onError(billingResult.debugMessage.ifBlank { "결제창을 열 수 없습니다" })
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) return

        val purchasedProductId = purchase.products.firstOrNull() ?: pendingProductId ?: return

        if (purchase.isAcknowledged) {
            onPurchaseSuccess(purchasedProductId)
            return
        }

        billingClient?.acknowledgePurchase(
            AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build(),
        ) { result ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                onPurchaseSuccess(purchasedProductId)
            } else {
                onError(result.debugMessage.ifBlank { "결제 확인에 실패했습니다" })
            }
        }
    }
}

@Composable
actual fun rememberInAppPurchaseLauncher(
    onPurchaseSuccess: (productId: String) -> Unit,
    onPurchaseCancelled: () -> Unit,
    onError: (message: String) -> Unit,
): InAppPurchaseLauncher {
    val activity = LocalContext.current as? Activity
    val launcher = remember(onPurchaseSuccess, onPurchaseCancelled, onError) {
        AndroidInAppPurchaseLauncher(
            activityProvider = { activity },
            onPurchaseSuccess = onPurchaseSuccess,
            onPurchaseCancelled = onPurchaseCancelled,
            onError = onError,
        )
    }

    DisposableEffect(launcher, activity) {
        launcher.attach(activity)
        onDispose { launcher.dispose() }
    }

    return launcher
}
