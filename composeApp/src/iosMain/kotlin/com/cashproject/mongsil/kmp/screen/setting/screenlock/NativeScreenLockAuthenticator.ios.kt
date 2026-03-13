package com.cashproject.mongsil.kmp.screen.setting.screenlock

import kotlin.coroutines.resume
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LABiometryTypeFaceID
import platform.LocalAuthentication.LABiometryTypeTouchID
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthentication

class IOSNativeScreenLockAuthenticator : NativeScreenLockAuthenticator {

    @OptIn(ExperimentalForeignApi::class)
    override fun availability(): NativeScreenLockAvailability {
        val context = LAContext()
        return if (context.canEvaluatePolicy(LAPolicyDeviceOwnerAuthentication, null)) {
            NativeScreenLockAvailability(
                isAvailable = true,
                title = "기기 잠금 바로 사용",
                description = when (context.biometryType) {
                    LABiometryTypeFaceID -> "iPhone에 저장된 Face ID 또는 기기 암호를 바로 사용할 수 있습니다."
                    LABiometryTypeTouchID -> "iPhone에 저장된 Touch ID 또는 기기 암호를 바로 사용할 수 있습니다."
                    else -> "iPhone에 저장된 기기 암호 또는 생체인증을 바로 사용할 수 있습니다."
                },
            )
        } else {
            NativeScreenLockAvailability(
                isAvailable = false,
                title = "기기 잠금 설정 필요",
                description = "iPhone 설정에서 Face ID, Touch ID 또는 기기 암호를 먼저 등록해야 합니다.",
            )
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun authenticate(reason: String): NativeScreenLockResult =
        suspendCancellableCoroutine { continuation ->
            val context = LAContext()
            if (!context.canEvaluatePolicy(LAPolicyDeviceOwnerAuthentication, null)) {
                continuation.resume(
                    NativeScreenLockResult.Failure("기기 인증을 사용할 수 없습니다.")
                )
                return@suspendCancellableCoroutine
            }

            context.evaluatePolicy(
                policy = LAPolicyDeviceOwnerAuthentication,
                localizedReason = reason,
            ) { success, error ->
                if (!continuation.isActive) return@evaluatePolicy

                val result = when {
                    success -> NativeScreenLockResult.Success
                    error == null -> NativeScreenLockResult.Cancelled
                    else -> NativeScreenLockResult.Failure(error.localizedDescription)
                }
                continuation.resume(result)
            }
        }
}
