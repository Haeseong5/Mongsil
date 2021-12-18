package com.cashproject.mongsil.ui.pages.calendar

import android.util.Log
import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.model.data.Comment
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.model.db.datasource.FirestoreDataSource
import com.cashproject.mongsil.model.db.datasource.LocalDataSource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class CalendarViewModel(private val localDataSource: LocalDataSource, private val firebaseDataSource: FirestoreDataSource) : BaseViewModel() {

    private val _sayingListData = MutableLiveData<List<Saying>>()
    val sayingListData: LiveData<List<Saying>>
        get() = _sayingListData

    private val _commentData = MutableLiveData<List<Comment>>()
    val commentData: LiveData<List<Comment>>
        get() = _commentData

    private val _sayingDataByDate = MutableLiveData<Saying>()
    val sayingDataByDate: LiveData<Saying>
        get() = _sayingDataByDate

    val db by lazy {
        Firebase.firestore
    }

    /**
     * 달력 날짜 클릭했을 때 리뷰가 없을 경우
     * Firestore 에서 해당 날짜의 명언을 가져온다.
     */
    fun getDataByDate(date: Date){
        firebaseDataSource.getDataByDate(date)
            .addOnSuccessListener { documents ->
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
}