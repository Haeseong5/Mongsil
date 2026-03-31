package com.cashproject.mongsil.kmp.firebase

import com.cashproject.mongsil.kmp.BuildConfig
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.remoteConfig
import kotlinx.serialization.json.Json

class AndroidAppUpdateChecker : AppUpdateChecker {

    private val remoteConfig = Firebase.remoteConfig

    override suspend fun fetchLatestVersion(): AppVersionInfo? {
        return try {
            remoteConfig.fetchAndActivate()
            val json = remoteConfig.getValue(APP_VERSION_KEY).asString()
            if (json.isBlank() || json == "{}") return null
            val parsed = Json.decodeFromString<RemoteAppVersion>(json)
            AppVersionInfo(
                latestVersionCode = parsed.latestAppVersionCode,
                latestVersionName = parsed.latestAppVersionName,
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun getCurrentVersionCode(): Int = BuildConfig.VERSION_CODE

    override fun getCurrentVersionName(): String = BuildConfig.VERSION_NAME

    companion object {
        private const val APP_VERSION_KEY = "appVersion"
    }
}

@kotlinx.serialization.Serializable
private data class RemoteAppVersion(
    val latestAppVersionCode: Int = 0,
    val latestAppVersionName: String = "-",
)
