package com.cashproject.mongsil.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentSplashBinding
import com.cashproject.mongsil.viewmodel.FirebaseViewModel

class SplashFragment : BaseFragment<FragmentSplashBinding, FirebaseViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_splash

    override val viewModel: FirebaseViewModel by viewModels()
    override fun initStartView() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}