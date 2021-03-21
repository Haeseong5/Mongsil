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
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.DateFormat
import java.util.*

class HomeViewModel(
    private val localDataSource: LocalDataSource
) : BaseViewModel() {

    private val _todayData = MutableLiveData<Saying>()
    val todayData: LiveData<Saying>
        get() = _todayData

    private val _commentData = MutableLiveData<List<Comment>>()
    val commentData: LiveData<List<Comment>>
        get() = _commentData

    val isCompletable = MutableLiveData<Boolean>(false)

    private val _isLike = MutableLiveData<Boolean>()
    val isLike: LiveData<Boolean>
        get() = _isLike

    val db by lazy {
        Firebase.firestore
    }

    private fun getLatestData() {
        loadingSubject.onNext(true)
        db.collection(COLLECTION)
            .orderBy(DATE, Query.Direction.DESCENDING) //최신 날짜 순으로 조회
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
//                    d("getLatestData", "${document.id} => ${document.data}")
                    val saying = document.toObject<Saying>().apply {
                        docId = document.id
                    }
                    _todayData.postValue(saying)
                }
                loadingSubject.onNext(false)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                loadingSubject.onNext(false)
            }
    }

    fun getTodayData() {
        loadingSubject.onNext(true)
        db.collection(COLLECTION)
            .whereEqualTo(DATE, dateToTimestamp(Date()))            //오늘 날짜 명언 가져오기
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                d("getTodayData", "Today Data ")
                if (documents.size() == 0){
                    getLatestData()
                }
                for (document in documents) {
                    d(TAG, "DocumentSnapshot data: ${document.data}")
                    val saying = document.toObject<Saying>().apply {
                        docId = document.id
                    }
                    d("DATA DATE", saying.date.time.toString())
                    d("DATA DATE", saying.date.toString())

                    _todayData.postValue(saying)
                }
                loadingSubject.onNext(false)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting today documents: ", exception)
                loadingSubject.onNext(false)
            }
    }

    fun getSayingData(docId: String) {
        loadingSubject.onNext(true)
        db.collection(COLLECTION).document(docId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
//                    d(TAG, "DocumentSnapshot data: ${document.data}")
                    val saying = document.toObject<Saying>().apply { this?.docId = document.id }
                    _todayData.postValue(saying)
                } else {
                    d(TAG, "No such document")
                }
                loadingSubject.onNext(false)
            }
            .addOnFailureListener { exception ->
                d(TAG, "get failed with ", exception)
                loadingSubject.onNext(false)
            }
    }

    fun like(saying: Saying) {
        addDisposable(
            localDataSource.insertLikeSaying(saying = saying)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.i(TAG, "success insertion")
                    _isLike.postValue(true)
                },
                    { error -> Log.e(TAG, "Unable to update username", error) }
                )
        )
    }

    fun unLike(docId: String) {
        addDisposable(localDataSource.deleteLikeSayingByDocId(docId = docId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.i(TAG, "success unlike")
//                _isLike.postValue(false)
            },
                { error -> Log.e(TAG, "Unable to update username", error) }
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
                    { _isLike.postValue(true) },
                    { error -> Log.e(TAG, "Unable to update username", error) },
                    { _isLike.postValue(false) }
                )
        )
    }

    fun getComments(docId: String) {
        addDisposable(
            localDataSource.getCommentsByDocId(docId)
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

    fun deleteCommentById(id: Int) {
        addDisposable(
            localDataSource.deleteCommentById(id)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    isCompletable.postValue(true)
                }, {

                })
        )
    }
}