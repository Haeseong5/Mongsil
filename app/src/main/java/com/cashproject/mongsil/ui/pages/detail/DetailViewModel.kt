package com.cashproject.mongsil.ui.pages.detail

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.data.db.entity.SayingEntity
import com.cashproject.mongsil.data.service.BookmarkService
import com.cashproject.mongsil.data.service.DiaryService
import com.cashproject.mongsil.ui.screen.calendar.detail.Comment
import com.cashproject.mongsil.ui.screen.calendar.detail.DetailUiModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class DetailViewModel (
    private val bookmarkService: BookmarkService=BookmarkService(),
//    private val diaryService: DiaryService,
) : ViewModel() {

    val _uiState: MutableStateFlow<DetailUiModel> = MutableStateFlow(DetailUiModel())
    val uiState: StateFlow<DetailUiModel> = _uiState

    private val _comments: MutableStateFlow<List<Comment>> = MutableStateFlow(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    private val _isLike = MutableLiveData<Boolean>()
    val isLike: LiveData<Boolean>
        get() = _isLike

    val db by lazy {
        Firebase.firestore
    }

//    init {
//        viewModelScope.launch {
//            _comments.emit(diaryService.getAllComments().map {
//                Comment(
//                    id = it.id,
//                    content = it.content,
//                    emotion = it.emotion,
//                    time = it.time,
//                    date = it.date
//                )
//            })
//        }

//        viewModelScope.launch {
//            _uiState.emit(
//                uiState.value.copy(
//                    id = 0,
//                    content = "",
//                    emotion = 0,
//                    time =,
//                    date =
//
//                )
//            )
//        }
//    }

//    fun getAllComments() {
//        viewModelScope.launch {
//            try {
//                val comments = diaryService.getAllComments()
//                _commentEntityList.value = comments
//            } catch (e: Exception) {
//                e.printStackTrace()
//                errorSubject.onNext(e)
//            }
//        }
//    }


    fun like(sayingEntity: SayingEntity) {
        viewModelScope.launch {
            try {
                bookmarkService.insertBookmarkPoster(sayingEntity)
            } catch (e: Exception) {
//                errorSubject.onNext(e)
            }
        }
    }

    fun unLike(docId: String) {
        viewModelScope.launch {
            try {
                bookmarkService.deleteBookmarkPoster(docId)
                _isLike.postValue(false)
            } catch (e: Exception) {
//                errorSubject.onNext(e)
            }
        }
    }


    fun findByDocId(docId: String) {
        try {
            viewModelScope.launch {
                val poster = bookmarkService.findPosterById(docId)
                _isLike.value = poster != null
            }
        } catch (e: Exception) {
//            errorSubject.onNext(e)
        }
    }
}