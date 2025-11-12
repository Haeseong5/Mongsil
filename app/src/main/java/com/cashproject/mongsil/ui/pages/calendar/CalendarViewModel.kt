package com.cashproject.mongsil.ui.pages.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.common.utils.log
import com.cashproject.mongsil.common.utils.printErrorLog
import com.cashproject.mongsil.repository.repository.DiaryRepository
import com.cashproject.mongsil.repository.repository.DiaryRepositoryImpl
import com.cashproject.mongsil.repository.repository.EmoticonRepository
import com.cashproject.mongsil.repository.repository.EmoticonRepositoryImpl
import com.cashproject.mongsil.repository.repository.PosterRepository
import com.cashproject.mongsil.ui.model.toEmoticon
import com.cashproject.mongsil.ui.pages.diary.model.Poster
import com.cashproject.mongsil.ui.pages.diary.model.toDomain
import com.cashproject.mongsil.ui.pages.diary.model.toPoster
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class CalendarViewModel(
    private val diaryRepository: DiaryRepository = DiaryRepositoryImpl(),
    private val posterRepository: PosterRepository = PosterRepository(),
    private val emoticonRepository: EmoticonRepository = EmoticonRepositoryImpl()
) : ViewModel() {

    private val _uiState: MutableStateFlow<CalendarUiState> =
        MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState>
        get() = _uiState.asStateFlow()

    private val _selectedPagePosition = MutableStateFlow<Int>(1)
    val selectedPagePosition = _selectedPagePosition.asStateFlow()

    val error = MutableSharedFlow<Throwable>()

    init {
        "calendarViewModel init".log()
        loadComments()
        loadEmoticons()
    }

    suspend fun getEmoticonUrl(emoticonId: Int): String {
        return try {
            emoticonRepository.getEmoticon(emoticonId)?.imageUrl ?: ""
        } catch (e: Exception) {
            e.printErrorLog()
            ""
        }
    }

    private fun loadEmoticons() {
        viewModelScope.launch {
            _uiState.update {
                uiState.value.copy(
                    emoticons = emoticonRepository.getEmoticons().toEmoticon()
                )
            }

        }
    }

    private fun loadComments() {
        viewModelScope.launchWithCatching {
            diaryRepository.loadDailyEmotions().collect { dailyEmoticons ->
                _uiState.update { uiState ->
                    uiState.copy(
                        calendarUiModel = dailyEmoticons.map { it.toUiModel() }
                    )
                }
            }
        }
    }

    fun getRandomSaying(date: Date): Poster {
        val posters: MutableList<Poster> = mutableListOf()
        viewModelScope.launch {
            posters.addAll(posterRepository.getAllPosters().toPoster())
        }
        return posterRepository.getRandomSaying(
            date = date,
            posters = posters.toDomain()
        ).toPoster()
    }

    private fun CoroutineScope.launchWithCatching(
        action: suspend () -> Unit = {}
    ) {
        launch {
            try {
                action.invoke()
            } catch (e: Exception) {
                error.emit(e)
            }
        }
    }
}