package com.cashproject.mongsil.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseActivity
import com.cashproject.mongsil.databinding.ActivityMainBinding
import com.cashproject.mongsil.di.Injection
import com.cashproject.mongsil.fcm.PushManager
import com.cashproject.mongsil.ui.dialog.ProgressDialog
import com.cashproject.mongsil.ui.main.MainViewModel
import com.cashproject.mongsil.viewmodel.ViewModelFactory
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.flow.collect
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

    val adMob: InterstitialAd by lazy { InterstitialAd(this@MainActivity) }

    private val viewModelFactory: ViewModelFactory by lazy {
        Injection.provideViewModelFactory(this@MainActivity)
    }

    val mainViewModel: MainViewModel by viewModels { viewModelFactory }

    private val pushManager = PushManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this, getString(R.string.ad_app_id))

        setupPushNotification()

        mainViewModel.apply {
            getSayingList()
            getAllComments()
            getAllLike()

            sayingList.observe(this@MainActivity) {
                printLog("success to load $it")
            }
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