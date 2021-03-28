package com.cashproject.mongsil.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.model.db.datasource.LocalDataSource
import io.reactivex.schedulers.Schedulers

class LockerViewModel(
    private val localDataSource: LocalDataSource
) : BaseViewModel() {

    private val _likeData = MutableLiveData<List<Saying>>()
    val likeData: LiveData<List<Saying>>
        get() = _likeData

    fun getAllLike() {
        addDisposable(
            localDataSource.getAllLikeData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    _likeData.postValue(it)
                },{
                    errorSubject.onNext(it)
                })
        )
    }
}