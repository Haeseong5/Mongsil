package com.cashproject.mongsil.base

import android.util.Log.d
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

open class BaseViewModel : ViewModel(){

    val TAG = this.javaClass.simpleName

    val errorSubject: BehaviorSubject<Throwable> = BehaviorSubject.create<Throwable>()

    val loadingSubject: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    private val compositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable){
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        d(TAG, "onCleared()")
        compositeDisposable.clear()
        super.onCleared()
    }
}