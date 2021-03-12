package com.cashproject.mongsil.viewmodel

import android.util.Log
import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cashproject.mongsil.base.ApplicationClass.Companion.COLLECTION
import com.cashproject.mongsil.base.ApplicationClass.Companion.DATE
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.model.data.Saying
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CalendarViewModel: BaseViewModel(){

    private val _sayingData = MutableLiveData<List<Saying>>()
    val sayingData: LiveData<List<Saying>>
        get() = _sayingData


    val db by lazy {
        Firebase.firestore
    }

    fun getData(){
        db.collection(COLLECTION)
            .orderBy(DATE, Query.Direction.DESCENDING) //최신 날짜 순으로 조회
            .limit(10)
            .get()
            .addOnSuccessListener { documents ->
                val result = ArrayList<Saying>()
                for (document in documents) {
                    d(TAG, "${document.id} => ${document.data}")
                    val saying = document.toObject<Saying>().apply {
                        docId = document.id
                    }
                    d(TAG, saying.toString())
                    result.add(saying)
                }
                _sayingData.postValue(result)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}