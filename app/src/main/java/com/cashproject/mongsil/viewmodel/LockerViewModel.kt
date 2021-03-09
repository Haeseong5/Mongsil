package com.cashproject.mongsil.viewmodel

import android.util.Log
import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.model.data.LikeSaying
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.model.db.LockerDao
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class LockerViewModel(private val localDataSource: LockerDao): BaseViewModel(){
    private val COLLECTION = "Mongsil"
    private val DATE = "date"

    private val _todayData = MutableLiveData<Saying>()
    val todayData: LiveData<Saying>
        get() = _todayData

    private val _likeData = MutableLiveData<List<LikeSaying>>()
    val likeData: LiveData<List<LikeSaying>>
        get() = _likeData


    val db by lazy {
        Firebase.firestore
    }
    fun insert(saying: LikeSaying): Completable{
        return localDataSource.insert(saying = saying)
    }

    fun getAllLike() {
        addDisposable(
            localDataSource.getAll()
                .subscribeOn(Schedulers.io())
                .subscribe({
                        _likeData.postValue(it)
                    },{

                    }))
    }

    fun getTodayData(){
        db.collection(COLLECTION)
            .orderBy(DATE, Query.Direction.DESCENDING) //최신 날짜 순으로 조회
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("GetToday", "${document.id} => ${document.data}")
                    val saying = document.toObject<Saying>().apply {
                        docId = document.id
                    }
                    _todayData.postValue(saying)
                    Log.d("GetToday", saying.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}