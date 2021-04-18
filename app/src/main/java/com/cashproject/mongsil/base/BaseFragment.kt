package com.cashproject.mongsil.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.cashproject.mongsil.di.Injection
import com.cashproject.mongsil.extension.addTo
import com.cashproject.mongsil.ui.MainActivity
import com.cashproject.mongsil.ui.dialog.ProgressDialog
import com.cashproject.mongsil.viewmodel.ViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment<T : ViewDataBinding, R : BaseViewModel> : Fragment(){
    val TAG: String = this.javaClass.simpleName

    val mainActivity by lazy { activity as MainActivity? }

    lateinit var binding: T

    abstract val viewModel: R

    lateinit var viewModelFactory: ViewModelFactory

    abstract val layoutResourceId: Int

    internal val compositeDisposable = CompositeDisposable()

    private lateinit var callback: OnBackPressedCallback

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog(requireContext())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        d(TAG, "++onAttach")
//        callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
////                findNavController().popBackStack()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        d(TAG, "++onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        d(TAG, "++onCreateView!!!")
        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)

        viewModelFactory = Injection.provideViewModelFactory(activity as Context)
        initStartView()
        return binding.root
    }

    abstract fun initStartView()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        d(TAG, "++onViewCreated!!!")
    }

    fun observeErrorEvent(){
        viewModel.errorSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                Log.e("error subject ", it.message.toString())
//                activity?.showToast(getString(com.cashproject.mongsil.R.string.network_state_error))
            }
            .addTo(compositeDisposable)
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
        compositeDisposable.clear()
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
        d(TAG, "++onDestroy!!!")
        compositeDisposable.clear()
        super.onDestroy()
    }

    fun addDisposable(disposable: Disposable){
        compositeDisposable.add(disposable)
    }

}