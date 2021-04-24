package com.cashproject.mongsil.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseActivity
import com.cashproject.mongsil.databinding.ActivityMainBinding
import com.cashproject.mongsil.ui.dialog.ProgressDialog
import com.google.android.gms.ads.*


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    val progressBar: ProgressDialog by lazy {
        ProgressDialog(this@MainActivity)
    }

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this, getString(R.string.ad_app_id))
//        makeStatusBarTransparent()
        progressBar.isProgress(true)

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
        mInterstitialAd.adUnitId = getString(R.string.ad_interstitial_video_id)
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