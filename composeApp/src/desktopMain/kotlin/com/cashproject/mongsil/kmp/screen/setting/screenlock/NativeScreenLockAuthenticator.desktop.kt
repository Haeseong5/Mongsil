package com.cashproject.mongsil.kmp.screen.setting.screenlock

import kotlinx.coroutines.runBlocking
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.screen_lock_auth_desktop_unavailable
import mongsil.composeapp.generated.resources.screen_lock_native_unavailable_desc_desktop
import mongsil.composeapp.generated.resources.screen_lock_native_unavailable_title_desktop
import org.jetbrains.compose.resources.getString

class DesktopNativeScreenLockAuthenticator : NativeScreenLockAuthenticator {
    override fun availability(): NativeScreenLockAvailability = runBlocking {
        NativeScreenLockAvailability(
            isAvailable = false,
            title = getString(Res.string.screen_lock_native_unavailable_title_desktop),
            description = getString(Res.string.screen_lock_native_unavailable_desc_desktop),
        )
    }

    override suspend fun authenticate(reason: String): NativeScreenLockResult =
        NativeScreenLockResult.Failure(getString(Res.string.screen_lock_auth_desktop_unavailable))
}
