package com.cashproject.mongsil.viewmodel

import android.util.Log
import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cashproject.mongsil.base.ApplicationClass.Companion.COLLECTION
import com.cashproject.mongsil.base.ApplicationClass.Companion.DATE
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.model.data.Comment
import com.cashproject.mongsil.model.data.LikeSaying
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.model.db.dao.CommentDao
import com.cashproject.mongsil.model.db.dao.LockerDao
import com.cashproject.mongsil.model.db.datasource.LocalDataSource
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SayingViewModel(
    private val localDataSource: LocalDataSource
) : BaseViewModel() {

    private val _todayData = MutableLiveData<Saying>()
    val todayData: LiveData<Saying>
        get() = _todayData

    private val _commentData = MutableLiveData<List<Comment>>()
    val commentData: LiveData<List<Comment>>
        get() = _commentData

    val isCompletable = MutableLiveData<Boolean>(false)

    val db by lazy {
        Firebase.firestore
    }

    fun getTodayData() {
        db.collection(COLLECTION)
            .orderBy(DATE, Query.Direction.DESCENDING) //최신 날짜 순으로 조회
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    d("GetToday", "${document.id} => ${document.data}")
                    val saying = document.toObject<Saying>().apply {
                        docId = document.id
                    }
                    _todayData.postValue(saying)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun like(saying: LikeSaying): Completable {
        return localDataSource.insertLikeSaying(saying = saying)
    }

    fun getComments(docId: String) {
        addDisposable(
            localDataSource.getComments(docId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _commentData.postValue(it)
                }, {

                })
        )
    }

    fun insertComment(comment: Comment) {
        addDisposable(
            localDataSource.insertComment(comment)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    isCompletable.postValue(true)
                }, {

                })
        )
    }
}