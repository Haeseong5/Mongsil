package com.cashproject.mongsil.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.extension.addTo
import com.cashproject.mongsil.extension.toSingleEvent
import com.cashproject.mongsil.model.data.Comment
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.model.db.datasource.FirestoreDataSource
import com.cashproject.mongsil.model.db.datasource.LocalDataSource
import com.google.firebase.firestore.ktx.toObject
import io.reactivex.schedulers.Schedulers
import kotlin.random.Random
import kotlin.random.nextInt

class MainViewModel(
    private val localDataSource: LocalDataSource,
    private val firestoreDataSource: FirestoreDataSource
) : BaseViewModel() {

    private val _sayingList = MutableLiveData<List<Saying>>()
    val sayingList get() = _sayingList

    private val _commentList = MutableLiveData<List<Comment>>()
    val commentList get() = _commentList

    /**HomeFragment*/
    //홈에 표시할 명언 데이터. onResume에 liveData 가 활성화되면서 번들데이터가 안보임;
    private val _sayingForHome = MutableLiveData<Saying>()
    val sayingForHome = _sayingForHome.toSingleEvent()

    fun getSayingList() {
        firestoreDataSource.getSayingList()
            .addOnSuccessListener { documents ->
                documents.map {
                    it.toObject<Saying>()
                }.let {
                    _sayingList.postValue(it)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    fun getAllComments() {
        localDataSource.getAllComments()
            .subscribeOn(Schedulers.io())
            .subscribe({
                _commentList.postValue(it)
            }, {
                errorSubject.onNext(it)
            }).addTo(compositeDisposable)
    }

    /**
     * 만약 오늘 보여줄 명언이 없으면, 가장 최신 명언으로 표시해주기
     */
    private fun getLatestData() {
        loadingSubject.onNext(true)
        firestoreDataSource.getLatestData()
            .addOnSuccessListener { documents ->
                Log.d("getLatestData", Thread.currentThread().name)
                for (document in documents) {
                    Log.d("getLatestData", "${document.id} => ${document.data}")
                    document.toObject<Saying>().apply {
                        docId = document.id
                    }.also {
                        _sayingForHome.postValue(it)
                    }
                }
                loadingSubject.onNext(false)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                loadingSubject.onNext(false)
                errorSubject.onNext(exception)
            }
    }

    /**
     * 홈화면
     */
    fun getTodaySaying() {
        loadingSubject.onNext(true)
        firestoreDataSource.getTodayData()
            .addOnSuccessListener { documents ->
                if (documents.size() == 0) {
                    getLatestData()
                }
                for (document in documents) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    document.toObject<Saying>().apply {
                        docId = document.id
                    }.also {
                        _sayingForHome.postValue(it)
                    }
                }
                loadingSubject.onNext(false)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting today documents: ", exception)
                loadingSubject.onNext(false)
                errorSubject.onNext(exception)
            }
    }

    fun insertComment(comment: Comment) {
        addDisposable(
            localDataSource.insertComment(comment)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val comments = (commentList.value ?: emptyList()) + comment
                    _commentList.postValue(comments)
                }, {
                    errorSubject.onNext(it)
                })
        )
    }

    fun deleteCommentById(id: Int) {
        addDisposable(
            localDataSource.deleteCommentById(id)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val comments = commentList.value?.filter { it.id != id }
                    _commentList.postValue(comments)
                }, {
                    errorSubject.onNext(it)
                })
        )
    }
}