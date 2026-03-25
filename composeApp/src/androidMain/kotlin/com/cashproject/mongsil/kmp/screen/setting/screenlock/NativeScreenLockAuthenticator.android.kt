package com.cashproject.mongsil.kmp.screen.setting.screenlock

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.screen_lock_auth_unavailable
import mongsil.composeapp.generated.resources.screen_lock_biometric_prompt_title
import mongsil.composeapp.generated.resources.screen_lock_native_available_desc_android
import mongsil.composeapp.generated.resources.screen_lock_native_available_title
import mongsil.composeapp.generated.resources.screen_lock_native_unavailable_desc_android
import mongsil.composeapp.generated.resources.screen_lock_native_unavailable_title
import org.jetbrains.compose.resources.getString
import kotlin.coroutines.resume

private const val ANDROID_ALLOWED_AUTHENTICATORS =
    BiometricManager.Authenticators.BIOMETRIC_STRONG or
        BiometricManager.Authenticators.DEVICE_CREDENTIAL

class AndroidNativeScreenLockAuthenticator(
    private val activityHolder: CurrentActivityHolder,
) : NativeScreenLockAuthenticator {

    override fun availability(): NativeScreenLockAvailability = runBlocking {
        val activity = activityHolder.get()
        val biometricManager = activity?.let(BiometricManager::from)
        val canAuthenticate = biometricManager?.canAuthenticate(ANDROID_ALLOWED_AUTHENTICATORS)

        when (canAuthenticate) {
            BiometricManager.BIOMETRIC_SUCCESS -> NativeScreenLockAvailability(
                isAvailable = true,
                title = getString(Res.string.screen_lock_native_available_title),
                description = getString(Res.string.screen_lock_native_available_desc_android),
            )
            else -> NativeScreenLockAvailability(
                isAvailable = false,
                title = getString(Res.string.screen_lock_native_unavailable_title),
                description = getString(Res.string.screen_lock_native_unavailable_desc_android),
            )
        }
    }

    override suspend fun authenticate(reason: String): NativeScreenLockResult {
        val activity = activityHolder.get()
            ?: return NativeScreenLockResult.Failure(getString(Res.string.screen_lock_auth_unavailable))

        return suspendCancellableCoroutine { continuation ->
            val prompt = BiometricPrompt(
                activity,
                ContextCompat.getMainExecutor(activity),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        if (continuation.isActive) {
                            continuation.resume(NativeScreenLockResult.Success)
                        }
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        if (!continuation.isActive) return

                        val result = when (errorCode) {
                            BiometricPrompt.ERROR_NEGATIVE_BUTTON,
                            BiometricPrompt.ERROR_USER_CANCELED,
                            BiometricPrompt.ERROR_CANCELED -> NativeScreenLockResult.Cancelled
                            else -> NativeScreenLockResult.Failure(errString.toString())
                        }
                        continuation.resume(result)
                    }

                    override fun onAuthenticationFailed() {
                        // 시스템 UI에서 계속 재시도할 수 있으므로 별도 처리하지 않는다.
                    }
                },
            )

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(runBlocking { getString(Res.string.screen_lock_biometric_prompt_title) })
                .setSubtitle(reason)
                .setAllowedAuthenticators(ANDROID_ALLOWED_AUTHENTICATORS)
                .build()

            prompt.authenticate(promptInfo)
            continuation.invokeOnCancellation { prompt.cancelAuthentication() }
        }
    }
}
