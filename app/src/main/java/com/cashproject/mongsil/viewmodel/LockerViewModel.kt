package com.cashproject.mongsil.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.model.data.LikeSaying
import com.cashproject.mongsil.model.db.dao.LockerDao
import com.cashproject.mongsil.model.db.datasource.LocalDataSource
import io.reactivex.schedulers.Schedulers

class LockerViewModel(private val localDataSource: LocalDataSource): BaseViewModel(){

    private val _likeData = MutableLiveData<List<LikeSaying>>()
    val likeData: LiveData<List<LikeSaying>>
        get() = _likeData

    fun getAllLike() {
        addDisposable(
            localDataSource.getAllLikeData()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                        _likeData.postValue(it)
                    },{

                    }))
    }
}