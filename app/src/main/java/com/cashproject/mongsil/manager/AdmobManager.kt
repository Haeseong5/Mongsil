package com.cashproject.mongsil.manager

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.interstitial.InterstitialAd

fun showInterstitialAd(interstitialAd: InterstitialAd?, activity: Activity) {
    if (interstitialAd != null) {
        interstitialAd.show(activity);
    } else {
        Log.d("TAG", "The interstitial ad wasn't ready yet.");
    }
}