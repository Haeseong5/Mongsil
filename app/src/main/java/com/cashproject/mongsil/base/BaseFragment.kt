package com.cashproject.mongsil.base

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
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
import io.reactivex.disposables.Disposable

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
//        requireActivity().window.apply {
//            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//
//            setFlags(
//
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//        }
        requireActivity().window.apply {
            this.statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        d(TAG, "onCreate")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        d(TAG, "onAttach")
    }

    override fun onStart() {
        super.onStart()
        d(TAG, "onStart")
    }
    override fun onResume() {
        super.onResume()
        d(TAG, "onResume")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        d(TAG, "onDestroyView")
    }

    override fun onPause() {
        super.onPause()
        d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        d(TAG, "onStop")
    }

    override fun onDetach() {
        super.onDetach()
        d(TAG, "onDetach()")
    }

    override fun onDestroy() {
        d(TAG, "onDestroy!!!")
        compositeDisposable.dispose()
        super.onDestroy()
    }

    fun addDisposable(disposable: Disposable){
        compositeDisposable.add(disposable)
    }

}