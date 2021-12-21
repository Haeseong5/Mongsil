package com.cashproject.mongsil.ui.main

import android.os.Bundle
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseActivity
import com.cashproject.mongsil.databinding.FragmentSplashBinding

class IntroActivity : BaseActivity<FragmentSplashBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}