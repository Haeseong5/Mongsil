package com.cashproject.mongsil.ui.pages.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.data.db.entity.Saying
import com.cashproject.mongsil.model.db.datasource.FirestoreDataSource
import com.cashproject.mongsil.data.db.LocalDataSource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DetailViewModel(
    private val localDataSource: LocalDataSource,
    private val firestoreDataSource: FirestoreDataSource
) : BaseViewModel() {

    private val _isLike = MutableLiveData<Boolean>()
    val isLike: LiveData<Boolean>
        get() = _isLike

    val db by lazy {
        Firebase.firestore
    }

    fun like(saying: Saying) {
        addDisposable(
            localDataSource.insertLikeSaying(saying)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.i(TAG, "success insertion")
                    _isLike.postValue(true)
                },
                    { error ->
                        Log.e(TAG, "Unable to update username", error)
                        errorSubject.onNext(error)
                    }
                )
        )
    }

    fun unLike(docId: String) {
        addDisposable(localDataSource.deleteLikeSayingByDocId(docId = docId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _isLike.postValue(false)
                Log.i(TAG, "success unlike")
            },
                { error ->
                    Log.e(TAG, "Unable to update username", error)
                    errorSubject.onNext(error)
                }
            )
        )
    }


    fun findByDocId(docId: String) {
        addDisposable(
            localDataSource.findByDocId(docId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    it.printStackTrace()
                }
                .subscribe(
                    {
                        _isLike.postValue(true)
                    },
                    { error ->
                        Log.e(TAG, "Unable to update username", error)
                        errorSubject.onNext(error)
                    },
                    {
                        _isLike.postValue(false)
                    }
                )
        )
    }
}