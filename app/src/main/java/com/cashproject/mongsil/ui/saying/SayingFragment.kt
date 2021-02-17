package com.cashproject.mongsil.ui.saying

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentHomeBinding
import com.cashproject.mongsil.databinding.FragmentSayingBinding
import com.cashproject.mongsil.model.data.Saying

class SayingFragment : BaseFragment<FragmentSayingBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_saying

    override fun initStartView() {
        arguments?.getString("saying")?.let {
            binding.saying = Saying(1, it)
        }
    }


}