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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(private val localDataSource: LocalDataSource, private val firestoreDataSource: FirestoreDataSource) : BaseViewModel() {

    /** CalendarFragment */
    //리사이클러뷰 데이터
    private val _sayingList = MutableLiveData<List<Saying>>()
    val sayingList: LiveData<List<Saying>>
        get() = _sayingList

    //캘린더뷰에 표시할 이모티콘 데이터
    private val _commentEmoticon = MutableLiveData<List<Comment>>()
    val commentEmoticon = _commentEmoticon.toSingleEvent()

    /**HomeFragment*/
    //홈에 표시할 명언 데이터. onResume에 liveData 가 활성화되면서 번들데이터가 안보임;
    private val _sayingForHome = MutableLiveData<Saying>()
    val sayingForHome = _sayingForHome.toSingleEvent()


    /**
     * 리사이클러뷰에 표시할 데이터 요청
     */
    fun getCalendarListData(date: Date) {
        firestoreDataSource.getCalendarListData(date)
            .addOnSuccessListener { documents ->
                val sayingList = ArrayList<Saying>()
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    document.toObject<Saying>().apply {
                        docId = document.id
                    }.also {
                        sayingList.add(it)
                    }
                }
                _sayingList.postValue(sayingList)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    /**
     * 캘린더뷰에 이모티콘을 표시하기 위한 댓글 데이터를 조회한다.
     */
    fun getAllCommentsForEmoticons() {
        localDataSource.getAllComments()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _commentEmoticon.postValue(it)
            }, {
                errorSubject.onNext(it)
            }).addTo(compositeDisposable)
    }

    //////////////////////////////////////////////////////////////////////////////

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
                if (documents.size() == 0){
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
}