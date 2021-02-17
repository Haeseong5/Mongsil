package com.cashproject.mongsil.base

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import io.reactivex.disposables.CompositeDisposable
//FragmentActivity는 Activity의 하위 클래스
abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity(){
    val TAG: String = this.javaClass.simpleName

    lateinit var binding: T

    abstract val layoutResourceId: Int

//    abstract val mainViewModel: R

    lateinit var progressDialog: ProgressDialog

    private val compositeDisposable = CompositeDisposable()

//    abstract fun initStartView()
//
//    abstract fun initBeforeBinding()
//
//    abstract fun initAfterBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutResourceId)
//        setFullView()
        progressDialog = ProgressDialog(this)
    }

    //뷰가 차지할 수 있는 영역을 상태바 및 소프트키 영역을 제외한 영역까지 확장해주는 역할
    private fun setFullView(){
        window.apply {  
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onCleared()")

        compositeDisposable.dispose()
        super.onDestroy()
    }
}