package com.cashproject.mongsil.kmp.screen.setting.screenlock

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.screen_lock_auth_device_unavailable
import mongsil.composeapp.generated.resources.screen_lock_native_available_desc_ios_faceid
import mongsil.composeapp.generated.resources.screen_lock_native_available_desc_ios_generic
import mongsil.composeapp.generated.resources.screen_lock_native_available_desc_ios_touchid
import mongsil.composeapp.generated.resources.screen_lock_native_available_title
import mongsil.composeapp.generated.resources.screen_lock_native_unavailable_desc_ios
import mongsil.composeapp.generated.resources.screen_lock_native_unavailable_title
import org.jetbrains.compose.resources.getString
import platform.LocalAuthentication.LABiometryTypeFaceID
import platform.LocalAuthentication.LABiometryTypeTouchID
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthentication
import kotlin.coroutines.resume

class IOSNativeScreenLockAuthenticator : NativeScreenLockAuthenticator {

    @OptIn(ExperimentalForeignApi::class)
    override fun availability(): NativeScreenLockAvailability = runBlocking {
        val context = LAContext()
        if (context.canEvaluatePolicy(LAPolicyDeviceOwnerAuthentication, null)) {
            NativeScreenLockAvailability(
                isAvailable = true,
                title = getString(Res.string.screen_lock_native_available_title),
                description = when (context.biometryType) {
                    LABiometryTypeFaceID -> getString(Res.string.screen_lock_native_available_desc_ios_faceid)
                    LABiometryTypeTouchID -> getString(Res.string.screen_lock_native_available_desc_ios_touchid)
                    else -> getString(Res.string.screen_lock_native_available_desc_ios_generic)
                },
            )
        } else {
            NativeScreenLockAvailability(
                isAvailable = false,
                title = getString(Res.string.screen_lock_native_unavailable_title),
                description = getString(Res.string.screen_lock_native_unavailable_desc_ios),
            )
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun authenticate(reason: String): NativeScreenLockResult =
        suspendCancellableCoroutine { continuation ->
            val context = LAContext()
            if (!context.canEvaluatePolicy(LAPolicyDeviceOwnerAuthentication, null)) {
                continuation.resume(
                    NativeScreenLockResult.Failure(runBlocking { getString(Res.string.screen_lock_auth_device_unavailable) })
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
