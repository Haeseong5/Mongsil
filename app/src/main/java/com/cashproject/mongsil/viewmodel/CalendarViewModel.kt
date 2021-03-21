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
import com.cashproject.mongsil.model.db.datasource.LocalDataSource
import com.cashproject.mongsil.util.DateUtil.dateToTimestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class CalendarViewModel(private val localDataSource: LocalDataSource) : BaseViewModel() {

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

    fun getData() {
        loadingSubject.onNext(true)

        db.collection(COLLECTION)
            .orderBy(DATE, Query.Direction.DESCENDING) //최신 날짜 순으로 조회
            .limit(10)
            .get()
            .addOnSuccessListener { documents ->
                val result = ArrayList<Saying>()
                for (document in documents) {
//                    d(TAG, "${document.id} => ${document.data}")
                    val saying = document.toObject<Saying>().apply {
                        docId = document.id
                    }
                    d(TAG, saying.toString())
                    result.add(saying)
                }
                _sayingData.postValue(result)
                loadingSubject.onNext(false)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }


    fun getDataByDate(date: Date){
        d("long", date.toString())

        db.collection(COLLECTION)
            .whereEqualTo(DATE, dateToTimestamp(date))
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                loadingSubject.onNext(true)
                for (document in documents) {
                    d(TAG, "${document.id} => ${document.data}")
                    val saying = document.toObject<Saying>().apply { docId = document.id }
                    _sayingDataByDate.postValue(saying)
                }
                loadingSubject.onNext(false)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                loadingSubject.onNext(false)
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

                })
        )
    }

}