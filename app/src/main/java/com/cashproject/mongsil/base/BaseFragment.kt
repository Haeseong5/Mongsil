package com.cashproject.mongsil.base

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.adapters.ProgressBarBindingAdapter
import androidx.fragment.app.Fragment
import com.cashproject.mongsil.R
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<T : ViewDataBinding, R : BaseViewModel> : Fragment(){
    val TAG: String = this.javaClass.simpleName

    lateinit var binding: T

    abstract val viewModel: R

    abstract val layoutResourceId: Int

    internal val compositeDisposable = CompositeDisposable()

    lateinit var progressDialog: AppCompatDialog

    abstract fun initStartView()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        d(TAG, "onCreateView!!!")
        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        progressDialog = AppCompatDialog(requireContext())
        initStartView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        d(TAG, "onViewCreated!!!")
    }

    fun isProgress(flag: Boolean) {
        if (flag) progressDialog.show() else progressDialog.dismiss()
    }
    fun setFullView(){
        activity?.window?.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    override fun onDestroy() {
        d(TAG, "onDestroy!!!")
        compositeDisposable.dispose()
        super.onDestroy()
    }
}