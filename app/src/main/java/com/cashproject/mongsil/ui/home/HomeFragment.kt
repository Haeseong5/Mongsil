package com.cashproject.mongsil.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentHomeBinding
import com.cashproject.mongsil.ui.viewmodel.FirebaseViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, FirebaseViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initStartView() {

    }

    override val viewModel: FirebaseViewModel
        get() = FirebaseViewModel()


}