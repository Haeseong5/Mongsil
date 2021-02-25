package com.cashproject.mongsil.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cashproject.mongsil.model.data.Saying
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

object FirebaseManager {
    private val TAG = this.javaClass.simpleName

    private val COLLECTION = "Mongsil"
    private val DATE = "date"

    private val _sayingData = MutableLiveData<List<Saying>>()
    val sayingData: LiveData<List<Saying>>
        get() = _sayingData

    val storage by lazy {
        FirebaseStorage.getInstance()
    }
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
                    Log.d(TAG, "${document.id} => ${document.data}")
                    result.add( document.toObject<Saying>() )
                }
                _sayingData.postValue(result)

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

}