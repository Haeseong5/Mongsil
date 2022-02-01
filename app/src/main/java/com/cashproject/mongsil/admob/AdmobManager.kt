package com.cashproject.mongsil.admob

import android.app.Activity
import android.content.Context
import android.util.Log
import com.cashproject.mongsil.R
import com.cashproject.mongsil.extension.errorLog
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd

class AdmobManager {}

fun showInterstitialAd(interstitialAd: InterstitialAd?, activity: Activity) {
    if (interstitialAd != null) {
        interstitialAd.show(activity);
    } else {
        Log.d("TAG", "The interstitial ad wasn't ready yet.");
    }
}