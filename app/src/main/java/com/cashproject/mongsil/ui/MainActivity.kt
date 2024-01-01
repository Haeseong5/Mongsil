package com.cashproject.mongsil.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseActivity
import com.cashproject.mongsil.data.firebase.RemoteConfigManager
import com.cashproject.mongsil.databinding.ActivityMainBinding
import com.cashproject.mongsil.di.Injection
import com.cashproject.mongsil.data.firebase.fcm.PushManager
import com.cashproject.mongsil.extension.getNavigationBarHeight
import com.cashproject.mongsil.ui.dialog.ProgressDialog
import com.cashproject.mongsil.ui.main.MainViewModel
import com.cashproject.mongsil.viewmodel.ViewModelFactory
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

    val progressBar: ProgressDialog by lazy { ProgressDialog(this@MainActivity) }

    private val viewModelFactory: ViewModelFactory by lazy {
        Injection.provideViewModelFactory(this@MainActivity)
    }

    val mainViewModel: MainViewModel by viewModels { viewModelFactory }

    private val pushManager = PushManager()
    private val remoteConfigManager by lazy { RemoteConfigManager(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding.root.setPadding(0, 0, 0, getNavigationBarHeight())
        MobileAds.initialize(this)
        setupPushNotification()
        remoteConfigManager.apply {
            initializeFirebaseRemoteConfig()
            setRemoteConfigListener()
        }
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