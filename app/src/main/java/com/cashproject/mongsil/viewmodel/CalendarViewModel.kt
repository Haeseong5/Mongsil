package com.cashproject.mongsil.viewmodel

import android.util.Log
import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cashproject.mongsil.base.ApplicationClass.Companion.COLLECTION
import com.cashproject.mongsil.base.ApplicationClass.Companion.DATE
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.model.data.Comment
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.model.db.datasource.FirestoreDataSource
import com.cashproject.mongsil.model.db.datasource.LocalDataSource
import com.cashproject.mongsil.util.DateUtil.dateToTimestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class CalendarViewModel(private val localDataSource: LocalDataSource, private val firebaseDataSource: FirestoreDataSource) : BaseViewModel() {

    private val _sayingData = MutableLiveData<List<Saying>>()
    val sayingData: LiveData<List<Saying>>
        get() = _sayingData

    private val _commentData = MutableLiveData<List<Comment>>()
    val commentData: LiveData<List<Comment>>
        get() = _commentData

    private val _sayingDataByDate = MutableLiveData<Saying>()
    val sayingDataByDate: LiveData<Saying>
        get() = _sayingDataByDate

    val db by lazy {
        Firebase.firestore
    }

    fun getCalendarListData(date: Date) {
        loadingSubject.onNext(true)

        firebaseDataSource.getCalendarListData(date)
            .addOnSuccessListener { documents ->
                val sayingList = ArrayList<Saying>()
                for (document in documents) {
                    d(TAG, "${document.id} => ${document.data}")
                    document.toObject<Saying>().apply {
                        docId = document.id
                    }.also {
                        sayingList.add(it)
                    }
                }
                _sayingData.postValue(sayingList)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                errorSubject.onNext(exception)
            }
    }


    fun getDataByDate(date: Date){
        firebaseDataSource.getDataByDate(date)
            .addOnSuccessListener { documents ->
                loadingSubject.onNext(true)
                for (document in documents) {
                    d(TAG, "${document.id} => ${document.data}")
                    document.toObject<Saying>().apply { docId = document.id }.also {
                        _sayingDataByDate.postValue(it)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                errorSubject.onNext(exception)
            }
    }


    fun getAllComments() {
        addDisposable(
            localDataSource.getAllComments()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _commentData.postValue(it)
                }, {
                    errorSubject.onNext(it)
                })
        )
    }

}