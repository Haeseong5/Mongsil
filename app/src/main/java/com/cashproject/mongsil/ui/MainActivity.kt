package com.cashproject.mongsil.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.cashproject.mongsil.BuildConfig
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseActivity
import com.cashproject.mongsil.data.firebase.fcm.PushManager
import com.cashproject.mongsil.databinding.ActivityMainBinding
import com.cashproject.mongsil.extension.getNavigationBarHeight
import com.cashproject.mongsil.extension.openPlayStore
import com.cashproject.mongsil.network.firebase.remoteconfig.AppVersion
import com.cashproject.mongsil.network.firebase.remoteconfig.RemoteConfigManager
import com.cashproject.mongsil.ui.dialog.CheckDialog
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.launch
import kotlin.compareTo

/**
 * MainActivity - MainViewModel
 *  MainFragment
 *    ViewPager
 *       - CalendarFragment
 *       - DetailFragment
 *       - LockerFragment
 */

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutResourceId: Int
        get() = R.layout.activity_main

    private val pushManager = PushManager()
    private val remoteConfigManager by lazy { RemoteConfigManager.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding.root.setPadding(0, 0, 0, getNavigationBarHeight())
        MobileAds.initialize(this)
        setupPushNotification()
        remoteConfigManager.apply {
            initializeFirebaseRemoteConfig()
            setRemoteConfigListener(
                activity = this@MainActivity,
                onResult = {
                    if (isOldVersion(it)) {
                        showAppVersionDialog(appVersion = it.latestAppVersionName)
                    }
                })
        }
    }

    fun showAppVersionDialog(
        appVersion: String,
    ) {
        CheckDialog(
            context = this,
            accept = { openPlayStore(this) },
            acceptText = "업데이트"
        ).also {
            it.start(
                getString(
                    R.string.app_version,
                    BuildConfig.VERSION_NAME,
                    appVersion
                )
            )
        }
    }


    /**
     * 10 < 11 -> true
     * 10 < 10 -> false
     * 9 < 10 -> false
     */
    fun isOldVersion(appVersion: AppVersion): Boolean {
        return BuildConfig.VERSION_CODE < appVersion.latestAppVersionCode
    }

    private fun setupPushNotification() {
        lifecycleScope.launch {
            PushManager.pushNotificationEvent.collect { isEnabled ->
                printLog("Push Notification Settings : $isEnabled")
                pushManager.updatePushNotificationSubscription(this@MainActivity, isEnabled)
            }
        }
    }

}