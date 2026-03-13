package com.cashproject.mongsil.kmp.screen.setting.screenlock

class DesktopNativeScreenLockAuthenticator : NativeScreenLockAuthenticator {
    override fun availability(): NativeScreenLockAvailability = NativeScreenLockAvailability(
        isAvailable = false,
        title = "기기 잠금 직접 연동 불가",
        description = "Desktop 버전은 운영체제 잠금을 직접 재사용하지 못해 앱 비밀번호 잠금 방식을 사용합니다.",
    )

    override suspend fun authenticate(reason: String): NativeScreenLockResult =
        NativeScreenLockResult.Failure("Desktop에서는 앱 비밀번호 잠금을 사용해 주세요.")
}
