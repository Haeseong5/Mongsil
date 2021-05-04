package com.cashproject.mongsil.ui.pages.home.detail

import android.util.Log
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel(
    private val localDataSource: LocalDataSource,
    private val firestoreDataSource: FirestoreDataSource
) : BaseViewModel() {

    private val _sayingLiveData = MutableLiveData<Saying>()
    val sayingLiveData: LiveData<Saying>
        get() = _sayingLiveData

    private val _commentData = MutableLiveData<List<Comment>>()
    val commentData: LiveData<List<Comment>>
        get() = _commentData

    val isUpdatedComment = MutableLiveData<Boolean>(false)

    private val _isLike = MutableLiveData<Boolean>()
    val isLike: LiveData<Boolean>
        get() = _isLike

    val db by lazy {
        Firebase.firestore
    }

    /**
     * 캘린더뷰에서 홈으로 넘어왔을 때 Firestore 에서 데이터 요청
     */
    fun getSingleSayingData(docId: String) {
        loadingSubject.onNext(true)
        firestoreDataSource.getSingleSayingData(docId)
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    val saying = document.toObject<Saying>().apply { this?.docId = document.id }
                    _sayingLiveData.postValue(saying)
                } else {
                    Log.d(TAG, "No such document")
                }
                loadingSubject.onNext(false)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
                loadingSubject.onNext(false)
                errorSubject.onNext(exception)
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
                Log.i(TAG, "success unlike")
//                _isLike.postValue(false)
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
                    { _isLike.postValue(true) },
                    { error ->
                        Log.e(TAG, "Unable to update username", error)
                        errorSubject.onNext(error)
                    },
                    { _isLike.postValue(false) }
                )
        )
    }

    fun getCommentsForHome(docId: String) {
        Log.d("--getComments ", Thread.currentThread().name)
        addDisposable(
            localDataSource.getCommentsByDocId(docId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _commentData.postValue(it)
                    Log.d("--getComments subscribe", Thread.currentThread().name)
                }, {
                    errorSubject.onNext(it)
                })
        )
    }

    fun insertComment(comment: Comment) {
        addDisposable(
            localDataSource.insertComment(comment)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    isUpdatedComment.postValue(true)
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
                    isUpdatedComment.postValue(true)
                }, {
                    errorSubject.onNext(it)
                })
        )
    }
}