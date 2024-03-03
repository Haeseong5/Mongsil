package com.cashproject.mongsil.ui.pages.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.common.utils.log
import com.cashproject.mongsil.logger.Logger
import com.cashproject.mongsil.repository.model.DailyEmoticon
import com.cashproject.mongsil.repository.repository.DiaryRepository
import com.cashproject.mongsil.repository.repository.DiaryRepositoryImpl
import com.cashproject.mongsil.repository.repository.EmoticonRepository
import com.cashproject.mongsil.repository.repository.EmoticonRepositoryImpl
import com.cashproject.mongsil.repository.repository.PosterRepository
import com.cashproject.mongsil.ui.model.toEmoticon
import com.cashproject.mongsil.ui.pages.diary.model.DiaryUiState
import com.cashproject.mongsil.ui.pages.diary.model.Poster
import com.cashproject.mongsil.ui.pages.diary.model.toComment
import com.cashproject.mongsil.ui.pages.diary.model.toDomain
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
    private val poster: Poster,
    private val date: Date,
    val isPagerItem: Boolean,
    private val diaryRepository: DiaryRepository = DiaryRepositoryImpl(),
    private val posterRepository: PosterRepository = PosterRepository(),
    private val emoticonRepository: EmoticonRepository = EmoticonRepositoryImpl()
) : ViewModel() {

    companion object {
        fun createViewModelFactory(
            poster: Poster,
            date: Date,
            isPagerItem: Boolean
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
                        return DiaryViewModel(
                            poster = poster,
                            date = date,
                            isPagerItem = isPagerItem,
                        ) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }
        }
    }

    private val _uiState: MutableStateFlow<DiaryUiState> = MutableStateFlow(
        DiaryUiState(
            date = date,
            poster = poster,
            isPagerItem = isPagerItem,
        )
    )
    val uiState: StateFlow<DiaryUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<DiarySideEffect>(extraBufferCapacity = 1)
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadEmotions()
        loadPoster(poster = poster)
        loadComments()
        posterRepository.toString().log()
    }

    private fun loadEmotions() {
        viewModelScope.launch {
            _uiState.emit(
                uiState.value.copy(
                    emoticons = emoticonRepository.getEmoticons().toEmoticon()
                )
            )
        }
    }

    fun updateUiState(action: DiaryUiState.() -> DiaryUiState) {
        _uiState.update { action.invoke(it) }
    }

    fun emitSideEffect(sideEffect: DiarySideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }

    private fun emitError(throwable: Throwable) {
        emitSideEffect(DiarySideEffect.Error(throwable))
    }

    private fun loadPoster(poster: Poster) {
        viewModelScope.launch {
            try {
                poster.toString().log()
                updateUiState {
                    copy(poster = poster)
                }
            } catch (e: Exception) {
                emitError(e)
            }
        }
    }

    private fun loadComments(selectedDate: Date = date) {
        viewModelScope.launch {
            try {
                val commentsFlow = diaryRepository.loadCommentListByDate(selectedDate)
                commentsFlow.collectLatest {
                    updateUiState {
                        copy(
                            comments = it.asReversed().toComment(),
                            dailyEmoticon = DailyEmoticon(
                                emoticonId = it.lastOrNull()?.emoticonId ?: 1,
                                date = selectedDate
                            )
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
                val comment = com.cashproject.mongsil.ui.pages.diary.model.Comment(
                    content = content,
                    emoticonId = emoticonId,
                    date = date,
                    time = Date()
                )
                diaryRepository.insert(comment.toDomain())
                Logger.sendLog(
                    key = "comment",
                    value = mapOf(
                        "length" to content.length,
                        "emoticonId" to emoticonId,
                    ),
                )
            } catch (e: Exception) {
                emitError(e)
            }
        }
    }

    fun deleteCommentById(id: Int) {
        viewModelScope.launch {
            try {
                diaryRepository.delete(id)
            } catch (e: Exception) {
                emitError(e)
            }
        }
    }
}