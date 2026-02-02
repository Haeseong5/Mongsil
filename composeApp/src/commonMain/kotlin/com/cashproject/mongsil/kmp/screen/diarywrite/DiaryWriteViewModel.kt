package com.cashproject.mongsil.kmp.screen.diarywrite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.repository.DiaryRepository
import com.cashproject.mongsil.kmp.screen.diarywrite.model.DiaryWriteEvent
import com.cashproject.mongsil.kmp.screen.diarywrite.model.DiaryWriteSideEffect
import com.cashproject.mongsil.kmp.screen.diarywrite.model.DiaryWriteUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 일기 작성 화면의 비즈니스 로직을 담당하는 ViewModel
 *
 * MVI 패턴을 따르며 다음 요소들로 구성됩니다:
 * - UiState: UI의 현재 상태
 * - Event: 사용자 액션
 * - SideEffect: 일회성 이벤트 (네비게이션, 토스트 등)
 */
class DiaryWriteViewModel(
    private val diaryRepository: DiaryRepository,
    year: Int,
    month: Int,
    day: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        DiaryWriteUiState(
            year = year,
            month = month,
            day = day
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = Channel<DiaryWriteSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        loadExistingDiary()
    }

    /**
     * 기존 일기가 있다면 불러옵니다.
     */
    private fun loadExistingDiary() {
        viewModelScope.launch {
            val existingDiary = diaryRepository.getDiaryByDate(
                year = _uiState.value.year,
                month = _uiState.value.month,
                day = _uiState.value.day
            )
            
            existingDiary?.let {
                _uiState.update { state ->
                    state.copy(content = it.content)
                }
            }
        }
    }

    /**
     * 사용자 이벤트를 처리합니다.
     */
    fun onEvent(event: DiaryWriteEvent) {
        when (event) {
            is DiaryWriteEvent.OnContentChange -> handleContentChange(event.content)
            is DiaryWriteEvent.OnSaveClick -> handleSaveClick()
            is DiaryWriteEvent.OnBackClick -> handleBackClick()
            is DiaryWriteEvent.OnBackPressed -> handleBackPressed()
            is DiaryWriteEvent.OnExitConfirm -> handleExitConfirm()
            is DiaryWriteEvent.OnExitCancel -> handleExitCancel()
        }
    }

    private fun handleContentChange(content: String) {
        _uiState.update { it.copy(content = content) }
    }

    private fun handleSaveClick() {
        val currentState = _uiState.value
        
        // 빈 내용은 저장하지 않음
        if (currentState.content.isBlank()) {
            viewModelScope.launch {
                _sideEffect.send(
                    DiaryWriteSideEffect.ShowError("일기 내용을 입력해주세요.")
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = diaryRepository.saveDiary(
                year = currentState.year,
                month = currentState.month,
                day = currentState.day,
                content = currentState.content
            )

            _uiState.update { it.copy(isLoading = false) }

            result.fold(
                onSuccess = {
                    _sideEffect.send(DiaryWriteSideEffect.ShowSaveSuccess)
                    _sideEffect.send(DiaryWriteSideEffect.NavigateToCalendar)
                },
                onFailure = { error ->
                    _sideEffect.send(
                        DiaryWriteSideEffect.ShowError(
                            error.message ?: "저장에 실패했습니다."
                        )
                    )
                }
            )
        }
    }

    private fun handleBackClick() {
        if (_uiState.value.hasContent) {
            _uiState.update { it.copy(showExitDialog = true) }
        } else {
            viewModelScope.launch {
                _sideEffect.send(DiaryWriteSideEffect.NavigateBack)
            }
        }
    }

    private fun handleBackPressed() {
        if (_uiState.value.hasContent) {
            _uiState.update { it.copy(showExitDialog = true) }
        } else {
            viewModelScope.launch {
                _sideEffect.send(DiaryWriteSideEffect.NavigateBack)
            }
        }
    }

    private fun handleExitConfirm() {
        _uiState.update { it.copy(showExitDialog = false) }
        viewModelScope.launch {
            _sideEffect.send(DiaryWriteSideEffect.NavigateBack)
        }
    }

    private fun handleExitCancel() {
        _uiState.update { it.copy(showExitDialog = false) }
    }
}
