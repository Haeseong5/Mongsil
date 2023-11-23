package com.cashproject.mongsil.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.data.api.PosterApi
import com.cashproject.mongsil.data.db.entity.CommentEntity
import com.cashproject.mongsil.data.db.entity.SayingEntity
import com.cashproject.mongsil.data.firebase.FireStoreDataSource
import com.cashproject.mongsil.data.firebase.fcm.PushManager
import com.cashproject.mongsil.data.repository.mapper.toPosters
import com.cashproject.mongsil.data.repository.model.Poster
import com.cashproject.mongsil.data.service.BookmarkService
import com.cashproject.mongsil.data.service.DiaryService
import com.cashproject.mongsil.data.service.PosterService
import com.cashproject.mongsil.ui.main.model.CalendarUiMapper
import com.cashproject.mongsil.ui.main.model.CalendarUiModel
import com.cashproject.mongsil.ui.main.model.CalendarUiState
import com.cashproject.mongsil.util.PreferencesManager
import com.cashproject.mongsil.util.timeMillisToLocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date
import kotlin.random.Random

/**
 * TODO Coroutines + Flow 적용
 * https://medium.com/firebase-developers/firebase-ing-with-kotlin-coroutines-flow-dab1bc364816
 */

class MainViewModel(
    private val diaryService: DiaryService,
    private val firestoreDataSource: FireStoreDataSource,
    private val posterApi: PosterApi = PosterService,
    private val bookmarkService: BookmarkService = BookmarkService(),
    private val pushManager: PushManager = PushManager(),
) : BaseViewModel() {

    // TODO ViewModel 화면 단위로 분리
    private val _calendarUiState: MutableStateFlow<CalendarUiState> =
        MutableStateFlow(CalendarUiState())
    val calendarUiState: StateFlow<CalendarUiState>
        get() = _calendarUiState.asStateFlow()

    private val _allPosters = MutableLiveData<List<Poster>>()
    val allPosters: LiveData<List<Poster>> get() = _allPosters

    private val _commentEntityList = MutableLiveData<List<CommentEntity>>()
    val commentEntityList: LiveData<List<CommentEntity>> get() = _commentEntityList

    private val _likeList = MutableLiveData<List<SayingEntity>>()
    val likeList: LiveData<List<SayingEntity>> get() = _likeList

    private val _selectedPagePosition = MutableStateFlow<Int>(1)
    val selectedPagePosition = _selectedPagePosition.asStateFlow()

    private val hashMap = HashMap<Long, Int>()

    init {
        initPushNotificationSettings()

        viewModelScope.launch {
            commentEntityList.asFlow().collect {
                _calendarUiState.emit(
                    calendarUiState.value.copy(
                        calendarUiModel = CalendarUiMapper.mapper(it)
                    )
                )
            }
        }
    }

    fun selectPage(position: Int) {
        _selectedPagePosition.value = position
    }

    fun getSayingList() {
        viewModelScope.launch {
            runCatching {
                posterApi.getAllPosters().toPosters()
            }.onSuccess {
                _allPosters.postValue(it)
            }.onFailure {
                errorSubject.onNext(it)
                Log.e(TAG, "Error getting documents: ", it)
            }
        }
    }

    fun getRandomSaying(date: Date): Poster {
        return try {
            val day = date.time
            val sayings = allPosters.value ?: emptyList()
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
            errorSubject.onNext(e)
            Poster("", "", "")
        }
    }

    fun getAllLike() {
        viewModelScope.launch {
            try {
                bookmarkService.getAllBookmarkedPosters().let {
                    _likeList.postValue(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorSubject.onNext(e)
            }
        }
    }

    fun getAllComments() {
        viewModelScope.launch {
            try {
                val comments = diaryService.getAllComments()
                _commentEntityList.value = comments
            } catch (e: Exception) {
                e.printStackTrace()
                errorSubject.onNext(e)
            }
        }
    }

    fun insertComment(commentEntity: CommentEntity) {
        viewModelScope.launch {
            try {
                diaryService.insertComment(commentEntity)
                val comments = (commentEntityList.value ?: emptyList()) + commentEntity
                _commentEntityList.postValue(comments)
            } catch (e: Exception) {
                e.printStackTrace()
                errorSubject.onNext(e)
            }
        }
    }

    fun deleteCommentById(id: Int) {
        viewModelScope.launch {
            try {
                diaryService.deleteComment(id)
                val comments = commentEntityList.value?.filter { it.id != id }
                _commentEntityList.postValue(comments!!)
            } catch (e: Exception) {
                errorSubject.onNext(e)
            }
        }
    }

    private fun initPushNotificationSettings() {
        pushManager.emitPushEvent(PreferencesManager.isEnabledPushNotification)
    }
}