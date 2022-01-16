package com.cashproject.mongsil.admob

import android.content.Context
import android.util.Log
import com.cashproject.mongsil.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.LoadAdError

class AdmobManager {}

fun InterstitialAd.showAdMob() {
    if (isLoaded) {
        show()
    } else {
        Log.i("TAG", "The interstitial wasn't loaded yet.");
    }
}


fun InterstitialAd.adMobInitial(context: Context) {
    // Create the InterstitialAd and set it up.
    adUnitId = context.getString(R.string.ad_interstitial_id)
    loadAd(AdRequest.Builder().build())
    adListener = (object : AdListener() {
        override fun onAdLoaded() {
            Log.i("onAdFailedToLoad", "onAdLoaded()")
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            val error = "domain: ${loadAdError.domain}, code: ${loadAdError.code}, " +
                    "message: ${loadAdError.message}"

            Log.i("onAdFailedToLoad", error)
        }

        override fun onAdClosed() {
            loadAd(AdRequest.Builder().build())
        }
    })
}