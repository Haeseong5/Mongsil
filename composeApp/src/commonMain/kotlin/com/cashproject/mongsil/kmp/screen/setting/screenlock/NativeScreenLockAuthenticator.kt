package com.cashproject.mongsil.kmp.screen.setting.screenlock

data class NativeScreenLockAvailability(
    val isAvailable: Boolean,
    val title: String,
    val description: String,
)

sealed interface NativeScreenLockResult {
    data object Success : NativeScreenLockResult
    data class Failure(val message: String? = null) : NativeScreenLockResult
    data object Cancelled : NativeScreenLockResult
}

interface NativeScreenLockAuthenticator {
    fun availability(): NativeScreenLockAvailability

    suspend fun authenticate(reason: String): NativeScreenLockResult
}
