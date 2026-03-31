package com.cashproject.mongsil.kmp.firebase

data class AppVersionInfo(
    val latestVersionCode: Int = 0,
    val latestVersionName: String = "-",
)

interface AppUpdateChecker {
    suspend fun fetchLatestVersion(): AppVersionInfo?
    fun getCurrentVersionCode(): Int
    fun getCurrentVersionName(): String
}
