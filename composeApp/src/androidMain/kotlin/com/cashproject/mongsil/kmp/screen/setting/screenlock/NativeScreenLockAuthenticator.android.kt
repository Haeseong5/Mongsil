package com.cashproject.mongsil.kmp.screen.setting.screenlock

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

private const val ANDROID_ALLOWED_AUTHENTICATORS =
    BiometricManager.Authenticators.BIOMETRIC_STRONG or
        BiometricManager.Authenticators.DEVICE_CREDENTIAL

class AndroidNativeScreenLockAuthenticator(
    private val activityHolder: CurrentActivityHolder,
) : NativeScreenLockAuthenticator {

    override fun availability(): NativeScreenLockAvailability {
        val activity = activityHolder.get()
        val biometricManager = activity?.let(BiometricManager::from)
        val canAuthenticate = biometricManager?.canAuthenticate(ANDROID_ALLOWED_AUTHENTICATORS)

        return when (canAuthenticate) {
            BiometricManager.BIOMETRIC_SUCCESS -> NativeScreenLockAvailability(
                isAvailable = true,
                title = "기기 잠금 바로 사용",
                description = "Android에 저장된 생체인증 또는 기기 잠금(PIN, 패턴, 비밀번호)을 바로 사용할 수 있습니다.",
            )
            else -> NativeScreenLockAvailability(
                isAvailable = false,
                title = "기기 잠금 설정 필요",
                description = "기기 설정에서 생체인증 또는 화면 잠금을 등록하지 않아 앱 전용 비밀번호 방식이 필요합니다.",
            )
        }
    }

    override suspend fun authenticate(reason: String): NativeScreenLockResult {
        val activity = activityHolder.get()
            ?: return NativeScreenLockResult.Failure("인증 화면을 열 수 없습니다.")

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
                .setTitle("화면 잠금")
                .setSubtitle(reason)
                .setAllowedAuthenticators(ANDROID_ALLOWED_AUTHENTICATORS)
                .build()

            prompt.authenticate(promptInfo)
            continuation.invokeOnCancellation { prompt.cancelAuthentication() }
        }
    }
}
