package com.cashproject.mongsil.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.extension.addTo
import com.cashproject.mongsil.model.data.Comment
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.model.db.datasource.FirestoreDataSource
import com.cashproject.mongsil.model.db.datasource.LocalDataSource
import com.google.firebase.firestore.ktx.toObject
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random

/**
 * TODO Coroutines + Flow 적용
 * https://medium.com/firebase-developers/firebase-ing-with-kotlin-coroutines-flow-dab1bc364816
 */

class MainViewModel(
    private val localDataSource: LocalDataSource,
    private val firestoreDataSource: FirestoreDataSource
) : BaseViewModel() {

    private val _sayingList = MutableLiveData<List<Saying>>()
    val sayingList: LiveData<List<Saying>> get() = _sayingList

    private val _commentList = MutableLiveData<List<Comment>>()
    val commentList: LiveData<List<Comment>> get() = _commentList

    private val _likeList = MutableLiveData<List<Saying>>()
    val likeList: LiveData<List<Saying>> get() = _likeList

    private val hashMap = HashMap<Long, Int>()

    fun getSayingList() {
        firestoreDataSource.getSayingList()
            .addOnSuccessListener { documents ->
                documents.map {
                    it.toObject<Saying>().apply { docId = it.id }
                }.let {
                    _sayingList.postValue(it)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: ", exception)
            }
    }

    fun getRandomSaying(date: Date): Saying {
        val day = date.time
        val sayings = sayingList.value ?: emptyList()
        val cachedIdx = hashMap[day]
        return if (cachedIdx == null) {
            val randomIdx = Random.nextInt(sayings.size)
            hashMap[day] = randomIdx
            sayings[randomIdx]
        } else {
            sayings[cachedIdx]
        }
    }

    fun getAllLike() {
        viewModelScope.launch {
            try {
                localDataSource.getAllLikeData().let {
                    _likeList.postValue(it)
                }
            } catch (e: Exception) {

            }
        }
    }

    fun getAllComments() {
        localDataSource.getAllComments()
            .subscribeOn(Schedulers.io())
            .subscribe({
                _commentList.postValue(it)
            }, {
                errorSubject.onNext(it)
                Log.e(TAG, it.localizedMessage.toString())
                Log.e(TAG, it.message.toString())
                Log.e(TAG, it.stackTraceToString())
            }).addTo(compositeDisposable)
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
                    Log.e(TAG, it.localizedMessage.toString())
                    Log.e(TAG, it.message.toString())
                    Log.e(TAG, it.stackTraceToString())
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