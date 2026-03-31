package com.cashproject.mongsil.kmp.firebase

class NoOpAppUpdateChecker : AppUpdateChecker {
    override suspend fun fetchLatestVersion(): AppVersionInfo? = null
    override fun getCurrentVersionCode(): Int = 0
    override fun getCurrentVersionName(): String = ""
}
