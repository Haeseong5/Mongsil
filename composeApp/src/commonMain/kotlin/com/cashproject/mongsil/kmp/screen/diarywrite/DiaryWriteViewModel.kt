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
        loadEmoticons()
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
     * 이모티콘 목록을 불러옵니다.
     */
    private fun loadEmoticons() {
        viewModelScope.launch {
            val emoticons = diaryRepository.getEmoticons()
            _uiState.update { it.copy(emoticons = emoticons) }
        }
    }

    /**
     * 사용자 이벤트를 처리합니다.
     */
    fun onEvent(event: DiaryWriteEvent) {
        when (event) {
            is DiaryWriteEvent.OnContentChange -> handleContentChange(event.content)
            is DiaryWriteEvent.OnEmoticonButtonClick -> handleEmoticonButtonClick()
            is DiaryWriteEvent.OnEmoticonSelected -> handleEmoticonSelected(event.emoticon)
            is DiaryWriteEvent.OnEmoticonBottomSheetDismiss -> handleEmoticonBottomSheetDismiss()
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

    private fun handleEmoticonButtonClick() {
        _uiState.update { it.copy(showEmoticonBottomSheet = true) }
    }

    private fun handleEmoticonSelected(emoticon: com.cashproject.mongsil.kmp.model.Emoticon) {
        _uiState.update { 
            it.copy(
                selectedEmoticon = emoticon,
                showEmoticonBottomSheet = false
            )
        }
    }

    private fun handleEmoticonBottomSheetDismiss() {
        _uiState.update { it.copy(showEmoticonBottomSheet = false) }
    }

    private fun handleSaveClick() {
        val currentState = _uiState.value
        
        // 빈 내용은 저장하지 않음
        if (currentState.content.isBlank()) {
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = diaryRepository.saveDiary(
                year = currentState.year,
                month = currentState.month,
                day = currentState.day,
                content = currentState.content,
                emoticonId = currentState.selectedEmoticon?.id?.toLong()
            )

            _uiState.update { it.copy(isLoading = false) }

            result.fold(
                onSuccess = {
                    _sideEffect.send(DiaryWriteSideEffect.SaveSuccess)
                },
                onFailure = { error ->
                    // TODO: 에러 처리를 외부에서 하거나 별도 방식으로 처리
                }
            )
        }
    }

    private fun handleBackClick() {
        if (_uiState.value.hasContent) {
            _uiState.update { it.copy(showExitDialog = true) }
        }
    }

    private fun handleBackPressed() {
        if (_uiState.value.hasContent) {
            _uiState.update { it.copy(showExitDialog = true) }
        }
    }

    private fun handleExitConfirm() {
        viewModelScope.launch {
            _uiState.update { it.copy(showExitDialog = false) }
            _sideEffect.send(DiaryWriteSideEffect.OnBack)
        }
    }

    private fun handleExitCancel() {
        _uiState.update { it.copy(showExitDialog = false) }
    }
}
