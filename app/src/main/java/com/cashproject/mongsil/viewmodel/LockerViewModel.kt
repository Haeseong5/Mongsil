package com.cashproject.mongsil.viewmodel

import android.util.Log
import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.model.data.Comment
import com.cashproject.mongsil.model.data.LikeSaying
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.model.db.CommentDao
import com.cashproject.mongsil.model.db.LockerDao
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LockerViewModel(private val localDataSource: LockerDao, private val commentDataSource: CommentDao): BaseViewModel(){

    private val COLLECTION = "Mongsil"
    private val DATE = "date"

    private val _todayData = MutableLiveData<Saying>()
    val todayData: LiveData<Saying>
        get() = _todayData

    private val _likeData = MutableLiveData<List<LikeSaying>>()
    val likeData: LiveData<List<LikeSaying>>
        get() = _likeData


    private val _commentData = MutableLiveData<List<Comment>>()
    val commentData: LiveData<List<Comment>>
        get() = _commentData

    val isCompletable = MutableLiveData<Boolean>(false)


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
                .observeOn(Schedulers.io())
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

    fun getComments(docId: String){
        d("getComments", docId)
        addDisposable(
            commentDataSource.getComments(docId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _commentData.postValue(it)
                    d("getComments", it.toString())
                },{

                })
        )
    }

    fun insertComment(comment: Comment){
        addDisposable(
            commentDataSource.insert(comment)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    isCompletable.postValue(true)
                },{

                })
        )
    }
}