package com.cashproject.mongsil.manager

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd

fun showInterstitialAd(interstitialAd: InterstitialAd?, activity: Activity) {
    if (interstitialAd != null) {
        interstitialAd.show(activity);
    } else {
        Log.d("TAG", "The interstitial ad wasn't ready yet.");
    }
}

fun setChildAdmobMode() {
    val requestConfiguration = MobileAds.getRequestConfiguration()
        .toBuilder()
        .setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
        .build()
    MobileAds.setRequestConfiguration(requestConfiguration)
}