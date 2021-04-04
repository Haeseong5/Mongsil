package com.cashproject.mongsil.ui

import android.os.Bundle
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseActivity
import com.cashproject.mongsil.databinding.ActivityMainBinding
import com.google.android.gms.ads.MobileAds


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this, getString(R.string.ad_app_id))
    }

}