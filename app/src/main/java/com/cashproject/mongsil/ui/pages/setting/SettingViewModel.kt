package com.cashproject.mongsil.ui.pages.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cashproject.mongsil.BuildConfig
import com.cashproject.mongsil.network.firebase.remoteconfig.RemoteConfigManager
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage


class SettingViewModel(
    val remoteConfigManager: RemoteConfigManager
) : ViewModel() {

    companion object {
        fun createViewModelFactory(
            remoteConfigManager: RemoteConfigManager
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
                        return SettingViewModel(
                            remoteConfigManager = remoteConfigManager
                        ) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }
        }
    }


    val storage = Firebase.storage("gs://mongsil-8dc44.appspot.com")
    var storageRef: StorageReference = storage.getReference()
    var backupRef: StorageReference? = storageRef.child("backup")

    fun isOldVersion(): Boolean {
        return BuildConfig.VERSION_CODE < remoteConfigManager.appVersion.latestAppVersionCode
    }
}