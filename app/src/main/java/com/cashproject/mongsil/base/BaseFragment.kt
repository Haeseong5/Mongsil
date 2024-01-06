package com.cashproject.mongsil.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.cashproject.mongsil.di.Injection
import com.cashproject.mongsil.ui.MainActivity
import com.cashproject.mongsil.viewmodel.ViewModelFactory

abstract class BaseFragment<T : ViewDataBinding> : SuperFragment() {

    var _binding: T? = null
    val binding get() = _binding!!
    val mainActivity by lazy { activity as? MainActivity }

    lateinit var viewModelFactory: ViewModelFactory

    abstract val layoutResourceId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModelFactory = Injection.provideViewModelFactory(activity as Context)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}