package com.cashproject.mongsil.base

import android.content.Context
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.cashproject.mongsil.di.Injection
import com.cashproject.mongsil.ui.MainActivity
import com.cashproject.mongsil.util.ClickUtil
import com.cashproject.mongsil.viewmodel.ViewModelFactory

abstract class BaseFragment<T : ViewDataBinding> : SuperFragment() {

    lateinit var binding: T

    val mainActivity by lazy { activity as? MainActivity }

    lateinit var viewModelFactory: ViewModelFactory

    abstract val layoutResourceId: Int

    val click by lazy { ClickUtil(this.lifecycle) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModelFactory = Injection.provideViewModelFactory(activity as Context)

        return binding.root
    }

}