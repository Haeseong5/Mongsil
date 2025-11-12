package com.cashproject.mongsil.network.firebase.remoteconfig

import android.app.Activity
import android.util.Log
import com.cashproject.mongsil.common.extensions.fromJson
import com.cashproject.mongsil.common.utils.log
import com.cashproject.mongsil.network.R
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

/**
 * https://medium.com/@orievictor123/android-in-app-updates-with-firebase-remote-config-5e6df335c491
 */
class RemoteConfigManager() {
    private val remoteConfig = Firebase.remoteConfig
    lateinit var appVersion: AppVersion


    companion object {
        private const val APP_VERSION_KEY = "appVersion"

        private var INSTANCE: RemoteConfigManager? = null

        fun getInstance(): RemoteConfigManager {
            return INSTANCE ?: synchronized(this) {
                RemoteConfigManager().also {
                    INSTANCE = it
                }
            }
        }
    }

    fun initializeFirebaseRemoteConfig() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 300
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    fun setRemoteConfigListener(activity: Activity, onResult: (AppVersion) -> Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(activity) { task ->
                try {
                    if (task.isSuccessful) {
                        appVersion = remoteConfig
                            .getValue(APP_VERSION_KEY)
                            .asString()
                            .fromJson<AppVersion>()

                        onResult.invoke(appVersion)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
    }

}