package com.cashproject.mongsil.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.extension.addTo
import com.cashproject.mongsil.extension.log
import com.cashproject.mongsil.data.firebase.fcm.PushManager
import com.cashproject.mongsil.data.db.entity.CommentEntity
import com.cashproject.mongsil.data.db.entity.SayingEntity
import com.cashproject.mongsil.data.db.LocalDataSource
import com.cashproject.mongsil.data.api.SayingApi
import com.cashproject.mongsil.data.api.SayingService
import com.cashproject.mongsil.data.firebase.FireStoreDataSource
import com.cashproject.mongsil.util.PreferencesManager
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random

/**
 * TODO Coroutines + Flow 적용
 * https://medium.com/firebase-developers/firebase-ing-with-kotlin-coroutines-flow-dab1bc364816
 */

class MainViewModel(
    private val localDataSource: LocalDataSource,
    private val firestoreDataSource: FireStoreDataSource,
    private val sayingApi: SayingApi = SayingService(),
    private val pushManager: PushManager = PushManager(),
) : BaseViewModel() {

    private val _sayingEntityList = MutableLiveData<List<SayingEntity>>()
    val sayingEntityList: LiveData<List<SayingEntity>> get() = _sayingEntityList

    private val _commentEntityList = MutableLiveData<List<CommentEntity>>()
    val commentEntityList: LiveData<List<CommentEntity>> get() = _commentEntityList

    private val _likeList = MutableLiveData<List<SayingEntity>>()
    val likeList: LiveData<List<SayingEntity>> get() = _likeList

    private val _selectedPagePosition = MutableStateFlow<Int>(1)
    val selectedPagePosition = _selectedPagePosition.asStateFlow()

    private val hashMap = HashMap<Long, Int>()

    init {
        initPushNotificationSettings()
    }

    fun selectPage(position: Int) {
        _selectedPagePosition.value = position
    }

    fun getSayingList() {
        viewModelScope.launch {
            runCatching {
                sayingApi.getSayings()
            }.onSuccess {
                _sayingEntityList.postValue(it)
            }.onFailure {
                Log.e(TAG, "Error getting documents: ", it)
            }
        }
    }

    fun getRandomSaying(date: Date): SayingEntity {
        return try {
            val day = date.time
            val sayings = sayingEntityList.value ?: emptyList()
            val cachedIdx = hashMap[day]
            if (cachedIdx == null) {
                val randomIdx = Random.nextInt(sayings.size)
                hashMap[day] = randomIdx
                sayings[randomIdx]
            } else {
                sayings[cachedIdx]
            }
        } catch (e: Exception) {
            Log.e(this.javaClass.name, e.localizedMessage)
            SayingEntity()
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
                "$it".log()
                _commentEntityList.postValue(it)
            }, {
                errorSubject.onNext(it)
                Log.e(TAG, it.localizedMessage.toString())
                Log.e(TAG, it.message.toString())
                Log.e(TAG, it.stackTraceToString())
            }).addTo(compositeDisposable)
    }

    fun insertComment(commentEntity: CommentEntity) {
        addDisposable(
            localDataSource.insertComment(commentEntity)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val comments = (commentEntityList.value ?: emptyList()) + commentEntity
                    "$comments".log()
                    _commentEntityList.postValue(comments)
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
                    val comments = commentEntityList.value?.filter { it.id != id }
                    _commentEntityList.postValue(comments!!)
                }, {
                    errorSubject.onNext(it)
                })
        )
    }

    private fun initPushNotificationSettings() {
        pushManager.emitPushEvent(PreferencesManager.isEnabledPushNotification)
    }
}