package com.cashproject.mongsil.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.cashproject.mongsil.di.Injection
import com.cashproject.mongsil.extension.addTo
import com.cashproject.mongsil.viewmodel.ViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun NavController.navigateSafely(fragment: Fragment){
    d("Navigation","Navigation")
    val currentBackStackFragment= (fragment.findNavController().currentBackStackEntry?.destination as? FragmentNavigator.Destination)?.className
    val thisClassName = fragment.javaClass.name
    if (currentBackStackFragment != thisClassName)
    d("Navigation", currentBackStackFragment.toString()+"sd")
    d("Navigation", thisClassName)
}

fun NavController.navigateSafely(view: View){
    val currentBackStackFragment= (view.findNavController().currentBackStackEntry?.destination as? FragmentNavigator.Destination)?.className
    val thisClassName = this.javaClass.simpleName
    d("Navigation", currentBackStackFragment.toString())
    d("Navigation", thisClassName)
}

abstract class BaseFragment<T : ViewDataBinding, R : BaseViewModel> : Fragment(){
    val TAG: String = this.javaClass.simpleName

    lateinit var binding: T

    abstract val viewModel: R

    lateinit var viewModelFactory: ViewModelFactory

    abstract val layoutResourceId: Int

    internal val compositeDisposable = CompositeDisposable()

    val progressDialog: com.cashproject.mongsil.ui.ProgressDialog by lazy {
        com.cashproject.mongsil.ui.ProgressDialog(requireContext())
    }

    abstract fun initStartView()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        d(TAG, "onCreateView!!!")
        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        progressDialog.apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }

        viewModelFactory = Injection.provideViewModelFactory(activity as Context)
        initStartView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        d(TAG, "onViewCreated!!!")
    }

    fun isProgress(flag: Boolean) {
        if (flag && !progressDialog.isShowing) progressDialog.show() else progressDialog.dismiss()
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