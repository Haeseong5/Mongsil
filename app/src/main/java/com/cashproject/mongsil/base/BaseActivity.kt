package com.cashproject.mongsil.base

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

//FragmentActivity는 Activity의 하위 클래스
abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity(){
    val TAG: String = this.javaClass.simpleName

    lateinit var binding: T

    abstract val layoutResourceId: Int

//    abstract val mainViewModel: R

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutResourceId)
    }

    //뷰가 차지할 수 있는 영역을 상태바 및 소프트키 영역을 제외한 영역까지 확장해주는 역할
    private fun setFullView(){
        window.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "++onStart()")
    }
    override fun onResume() {
        Log.d(TAG, "++onResume()")
        super.onResume()
    }
    override fun onPause() {
        Log.d(TAG, "++onPause()")
        super.onPause()
    }
    override fun onStop() {
        Log.d(TAG, "++onStop()")
        super.onStop()
    }
    override fun onDestroy() {
        Log.d(TAG, "++onDestroy()")
        compositeDisposable.dispose()
        super.onDestroy()
    }
    fun addDisposable(disposable: Disposable){
        compositeDisposable.add(disposable)
    }
}