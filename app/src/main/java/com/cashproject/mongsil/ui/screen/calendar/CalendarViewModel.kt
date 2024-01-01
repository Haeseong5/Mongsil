package com.cashproject.mongsil.ui.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.data.repository.DiaryRepository
import com.cashproject.mongsil.repository.DiaryRepositoryImpl
import com.cashproject.mongsil.ui.main.model.CalendarUiState
import com.cashproject.mongsil.ui.main.model.toUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
class CalendarViewModel(
    private val diaryRepository: DiaryRepository = DiaryRepositoryImpl(),
) : ViewModel() {

    val error = MutableSharedFlow<Throwable>()

    private val _uiState: MutableStateFlow<CalendarUiState> =
        MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState>
        get() = _uiState.asStateFlow()

    init {
        loadComments()
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