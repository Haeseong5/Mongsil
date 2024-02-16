package com.cashproject.mongsil.network.firebase.remoteconfig

import android.app.Activity
import android.util.Log
import com.cashproject.mongsil.common.extensions.fromJson
import com.cashproject.mongsil.common.utils.log
import com.cashproject.mongsil.network.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

/**
 * https://medium.com/@orievictor123/android-in-app-updates-with-firebase-remote-config-5e6df335c491
 */
class RemoteConfigManager(private val activity: Activity) {
    private val remoteConfig = Firebase.remoteConfig
    lateinit var appVersion: AppVersion

    companion object {
        private const val APP_VERSION_KEY = "appVersion"
    }

    fun initializeFirebaseRemoteConfig() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 300
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    fun setRemoteConfigListener() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(activity) { task ->
                try {
                    "### ${remoteConfig.getString(APP_VERSION_KEY)}".log()
                    "### ${remoteConfig.getLong("latestAppVersionCode")}".log()

                    if (task.isSuccessful) {
                        appVersion =
                            remoteConfig.getValue(APP_VERSION_KEY).asString().fromJson<AppVersion>()
                        if (isOldVersion()) {
                            showAppVersionDialog()
                        }

                        val updated = task.result
                        Log.i(
                            "###" + this.javaClass.name,
                            "Config params updated: $updated\t" +
                                    "latestAppVersionCode : ${appVersion.latestAppVersionCode}\t" +
                                    "latestAppVersionName : ${appVersion.latestAppVersionName}"
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
    }

    // TODO 수정
    private fun showAppVersionDialog() {
//        CheckDialog(
//            context = activity,
//            accept = { openPlayStore(activity ?: return@CheckDialog) },
//            acceptText = "업데이트"
//        ).also {
//            it.start(
//                activity.getString(
//                    R.string.app_version,
//                    BuildConfig.VERSION_NAME,
//                    com.cashproject.mongsil.common.App.appVersion.latestAppVersionName
//                )
//            )
//        }
    }
}

/**
 * 10 < 11 -> true
 * 10 < 10 -> false
 * 9 < 10 -> false
 */
// TODO 수정
fun isOldVersion(): Boolean {
    return false
//    return BuildConfig.VERSION_CODE < com.cashproject.mongsil.common.App.appVersion.latestAppVersionCode
}