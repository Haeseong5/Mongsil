package com.cashproject.mongsil.ui.saying

import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentHomeBinding
import com.cashproject.mongsil.databinding.FragmentSayingBinding
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.viewmodel.FirebaseViewModel
import com.google.firebase.Timestamp

class SayingFragment : BaseFragment<FragmentSayingBinding, FirebaseViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_saying

    override val viewModel: FirebaseViewModel
        get() = FirebaseViewModel()

    override fun initStartView() {
        arguments?.getString("saying")?.let {
//            binding.saying = Saying("1", it, Timestamp.now())
        }
    }


}