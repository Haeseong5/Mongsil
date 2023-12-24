package com.cashproject.mongsil.ui.pages.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.data.db.entity.SayingEntity
import com.cashproject.mongsil.data.db.entity.toEmoticon
import com.cashproject.mongsil.data.service.DiaryService
import com.cashproject.mongsil.repository.PosterRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date


class DiaryViewModel(
    val date: Date,
    private val diaryService: DiaryService = DiaryService(),
    private val posterRepository: PosterRepository = PosterRepository(),
) : ViewModel() {

    companion object {
        fun createViewModelFactory(date: Date): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
                        return DiaryViewModel(
                            date = date,
                        ) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }
        }
    }

    private val _uiState: MutableStateFlow<DiaryUiState> = MutableStateFlow(DiaryUiState())
    val uiState: StateFlow<DiaryUiState> = _uiState.asStateFlow()

    private val _error = MutableSharedFlow<Throwable>(extraBufferCapacity = 1)
    val error = _error.asSharedFlow()

    init {
        loadPoster()
        loadComments()
    }

    fun updateUiState(action: DiaryUiState.() -> DiaryUiState) {
        _uiState.update { action.invoke(it) }
    }

    private fun emitError(throwable: Throwable) {
        viewModelScope.launch {
            _error.emit(throwable)
        }
    }

    private fun loadPoster() {
        viewModelScope.launch {
            try {
                val poster = posterRepository.getRandomSinglePoster()
                updateUiState {
                    copy(posterUrl = poster.image)
                }
            } catch (e: Exception) {
                emitError(e)
            }
        }
    }

    private fun loadComments(selectedDate: Date = date) {
        viewModelScope.launch {
            try {
                val commentsFlow = diaryService.loadCommentsByDate(selectedDate)
                commentsFlow.collectLatest {
                    updateUiState {
                        copy(
                            comments = it.asReversed()
                        )
                    }
                }
            } catch (e: Exception) {
                emitError(e)
            }
        }
    }

    fun submitComment(
        content: String,
        emoticonId: Int = uiState.value.emoticonId,
        date: Date = this.date
    ) {
        viewModelScope.launch {
            try {
                val comment = Comment(
                    content = content,
                    emoticon = emoticonId.toEmoticon(),
                    date = date,
                    time = Date()
                )
                diaryService.insertComment(comment.toEntity())
            } catch (e: Exception) {
                emitError(e)
            }
        }
    }

    fun deleteCommentById(id: Int) {
        viewModelScope.launch {
            try {
                diaryService.deleteComment(id)
            } catch (e: Exception) {
                emitError(e)
            }
        }
    }


    fun like(sayingEntity: SayingEntity) { }
//        viewModelScope.launch {
//            try {
//                bookmarkService.insertBookmarkPoster(sayingEntity)
//            } catch (e: Exception) {
////                errorSubject.onNext(e)
//            }
//        }
//    }
//
    fun unLike(docId: String) { }
//        viewModelScope.launch {
//            try {
//                bookmarkService.deleteBookmarkPoster(docId)
////                _isLike.postValue(false)
//            } catch (e: Exception) {
////                errorSubject.onNext(e)
//            }
//        }
//    }


    fun findByDocId(docId: String) { }
//        try {
//            viewModelScope.launch {
//                val poster = bookmarkService.findPosterById(docId)
////                _isLike.value = poster != null
//            }
//        } catch (e: Exception) {
////            errorSubject.onNext(e)
//        }
//    }
}