package com.cashproject.mongsil.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log.d
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentHomeBinding
import com.cashproject.mongsil.firebase.FirebaseManager.storage
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.viewmodel.FirebaseViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, FirebaseViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_home

    override val viewModel: FirebaseViewModel by viewModels()
    override fun initStartView() {
        setFullView()

        arguments?.let {
//            binding.saying = Saying("1", it, Timestamp.now())
            val saying = Saying(
                docId = it.getString("docId"),
                image = it.getString("image"))
            binding.saying = saying
        }

        viewModel.getTodayData()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.todayData.observe(viewLifecycleOwner, Observer {
            binding.saying = it
            d("today", it.toString())
        })
    }

}