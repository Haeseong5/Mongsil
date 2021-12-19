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

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {
    val TAG: String = this.javaClass.simpleName

    lateinit var binding: T

    val mainActivity by lazy { activity as? MainActivity }

    lateinit var viewModelFactory: ViewModelFactory

    abstract val layoutResourceId: Int

    val click by lazy { ClickUtil(this.lifecycle) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        d(TAG, "++onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        d(TAG, "++onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        d(TAG, "++onCreateView!!!")
        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        binding.lifecycleOwner = this
        viewModelFactory = Injection.provideViewModelFactory(activity as Context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        d(TAG, "++onViewCreated!!!")
    }

    override fun onStart() {
        super.onStart()
        d(TAG, "++onStart")
    }

    override fun onResume() {
        super.onResume()
        d(TAG, "++onResume")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        d(TAG, "++onDestroyView")
    }

    override fun onPause() {
        super.onPause()
        d(TAG, "++onPause")
    }

    override fun onStop() {
        super.onStop()
        d(TAG, "++onStop")
    }

    override fun onDetach() {
        super.onDetach()
        d(TAG, "++onDetach()")
    }

    override fun onDestroy() {
        super.onDestroy()
        d(TAG, "++onDestroy!!!")
    }

}