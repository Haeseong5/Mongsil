package com.cashproject.mongsil.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseActivity
import com.cashproject.mongsil.databinding.ActivityMainBinding
import com.cashproject.mongsil.di.Injection
import com.cashproject.mongsil.ui.dialog.ProgressDialog
import com.cashproject.mongsil.ui.main.MainViewModel
import com.cashproject.mongsil.viewmodel.ViewModelFactory
import com.google.android.gms.ads.*
import java.util.*

//여기다가 프로그레스바 박아도 될 듯
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    val progressBar: ProgressDialog by lazy {
        ProgressDialog(this@MainActivity)
    }

    private lateinit var mInterstitialAd: InterstitialAd

    private val viewModelFactory: ViewModelFactory by lazy {
        Injection.provideViewModelFactory(this@MainActivity)
    }

    val mainViewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this, getString(R.string.ad_app_id))

        //Calendar
        mainViewModel.getCalendarListData(Date(Calendar.getInstance().timeInMillis)) //read firestore -> display saying in RecyclerView
        mainViewModel.getAllCommentsForEmoticons() //read room db -> display comment in CalendarView

        //Home
        mainViewModel.getTodaySaying()
    }

    fun showAdMob(){
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            Log.i("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    fun adMobInitial(){
        // Create the InterstitialAd and set it up.
        mInterstitialAd = InterstitialAd(this@MainActivity)
        mInterstitialAd.adUnitId = getString(R.string.ad_interstitial_id)
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener = (
                object : AdListener() {
                    override fun onAdLoaded() {
                        Log.i("onAdFailedToLoad", "onAdLoaded()")
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        val error = "domain: ${loadAdError.domain}, code: ${loadAdError.code}, " +
                                "message: ${loadAdError.message}"

                        Log.i("onAdFailedToLoad", error)
                    }

                    override fun onAdClosed() {
                        mInterstitialAd.loadAd(AdRequest.Builder().build())
                    }
                }
        )
    }

}