package com.cashproject.mongsil.kmp.screen.diarywrite

import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.data.DiaryRepository
import com.cashproject.mongsil.kmp.core.data.EmoticonRepository
import com.cashproject.mongsil.kmp.core.datastore.LocalPreferences
import com.cashproject.mongsil.kmp.screen.diarywrite.model.DiaryWriteEvent
import com.cashproject.mongsil.kmp.screen.diarywrite.model.DiaryWriteSideEffect
import com.cashproject.mongsil.kmp.screen.diarywrite.model.DiaryWriteUiState
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
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
    private val emoticonRepository: EmoticonRepository,
    private val localPreferences: LocalPreferences,
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
        loadInitialData()
    }

    /**
     * 이모티콘·일기·잠금해제 목록을 병렬로 로드하여 초기화 시간을 단축합니다.
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            val emoticonsDeferred = async {
                emoticonRepository.getEmoticons().getOrElse { emptyList() }
            }
            val diaryDeferred = async {
                diaryRepository.getDiaryByDate(
                    year = _uiState.value.year,
                    month = _uiState.value.month,
                    day = _uiState.value.day
                )
            }
            val unlockedDeferred = async {
                localPreferences.getStringSet(KEY_UNLOCKED_PREMIUMS)
                    .firstOrNull()
                    ?.mapNotNull { it.toIntOrNull() }
                    ?.toSet()
                    ?: emptySet()
            }

            val emoticons = emoticonsDeferred.await()
            val diary = diaryDeferred.await()
            val unlockedIds = unlockedDeferred.await()

            val selectedEmoticon = if (diary != null) {
                diary.emoticonId?.let { id -> emoticons.find { it.id == id.toInt() } }
            } else {
                emoticons.filter { !it.isPremium }.take(4).randomOrNull()
            }

            val loadedContent = diary?.content ?: ""
            val loadedPhotoUris = parsePhotoUris(diary?.photoUri)
            val loadedTextAlign = diary?.textAlign?.toTextAlign() ?: TextAlign.Start

            _uiState.update { state ->
                state.copy(
                    emoticons = emoticons,
                    content = loadedContent,
                    photoUris = loadedPhotoUris,
                    selectedEmoticon = selectedEmoticon,
                    isExistingDiary = diary != null,
                    isInitializing = false,
                    savedContent = loadedContent,
                    savedPhotoUris = loadedPhotoUris,
                    savedEmoticonId = selectedEmoticon?.id,
                    unlockedPremiumIds = unlockedIds,
                    textAlign = loadedTextAlign,
                    savedTextAlign = loadedTextAlign,
                )
            }
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
            is DiaryWriteEvent.OnPhotosSelected -> handlePhotosSelected(event.photoUris)
            is DiaryWriteEvent.OnPhotoRemoved -> handlePhotoRemoved(event.index)
            is DiaryWriteEvent.OnSaveClick -> handleSaveClick()
            is DiaryWriteEvent.OnBackClick -> handleBackClick()
            is DiaryWriteEvent.OnBackPressed -> handleBackPressed()
            is DiaryWriteEvent.OnExitConfirm -> handleExitConfirm()
            is DiaryWriteEvent.OnExitCancel -> handleExitCancel()
            is DiaryWriteEvent.OnDeleteClick -> handleDeleteClick()
            is DiaryWriteEvent.OnDeleteConfirm -> handleDeleteConfirm()
            is DiaryWriteEvent.OnDeleteCancel -> handleDeleteCancel()
            is DiaryWriteEvent.OnPremiumEmoticonClick -> handlePremiumEmoticonClick(event.emoticon)
            is DiaryWriteEvent.OnAdRewardEarned -> handleAdRewardEarned(event.emoticonId)
            is DiaryWriteEvent.OnAdDismissed -> Unit
            is DiaryWriteEvent.OnTextAlignToggle -> handleTextAlignToggle()
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

    private fun handlePhotosSelected(photoUris: List<String>) {
        if (photoUris.isEmpty()) return
        _uiState.update { state ->
            state.copy(photoUris = state.photoUris + photoUris)
        }
    }

    private fun handlePhotoRemoved(index: Int) {
        _uiState.update { state ->
            if (index !in state.photoUris.indices) return@update state
            state.copy(
                photoUris = state.photoUris.filterIndexed { i, _ -> i != index }
            )
        }
    }

    private fun handleSaveClick() {
        val currentState = _uiState.value
        
        // 빈 내용은 저장하지 않음
        if (currentState.content.isBlank() && currentState.photoUris.isEmpty()) {
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = diaryRepository.saveDiary(
                year = currentState.year,
                month = currentState.month,
                day = currentState.day,
                content = currentState.content,
                emoticonId = currentState.selectedEmoticon?.id?.toLong(),
                photoUri = serializePhotoUris(currentState.photoUris),
                textAlign = currentState.textAlign.toDbString(),
            )

            _uiState.update { it.copy(isLoading = false) }

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(savedTextAlign = it.textAlign) }
                    _sideEffect.send(DiaryWriteSideEffect.SaveSuccess)
                },
                onFailure = { error ->
                    // TODO: 에러 처리를 외부에서 하거나 별도 방식으로 처리
                }
            )
        }
    }

    private fun handleBackClick() {
        if (uiState.value.hasUnsavedChanges) {
            _uiState.update { it.copy(showExitDialog = true) }
        } else {
            viewModelScope.launch {
                _sideEffect.send(DiaryWriteSideEffect.OnBack)
            }
        }
    }

    private fun handleBackPressed() {
        if (_uiState.value.hasUnsavedChanges) {
            _uiState.update { it.copy(showExitDialog = true) }
        } else {
            viewModelScope.launch {
                _sideEffect.send(DiaryWriteSideEffect.OnBack)
            }
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

    private fun handleDeleteClick() {
        _uiState.update { it.copy(showDeleteDialog = true) }
    }

    private fun handleDeleteConfirm() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, showDeleteDialog = false) }
            diaryRepository.deleteDiary(state.year, state.month, state.day)
                .fold(
                    onSuccess = {
                        _sideEffect.send(DiaryWriteSideEffect.DeleteSuccess)
                    },
                    onFailure = {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                )
        }
    }

    private fun handleDeleteCancel() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    private fun handlePremiumEmoticonClick(emoticon: com.cashproject.mongsil.kmp.model.Emoticon) {
        viewModelScope.launch {
            _uiState.update { it.copy(showEmoticonBottomSheet = false) }
            _sideEffect.send(DiaryWriteSideEffect.ShowRewardedAd(emoticon.id))
        }
    }

    private fun handleAdRewardEarned(emoticonId: Int) {
        viewModelScope.launch {
            val newUnlocked = _uiState.value.unlockedPremiumIds + emoticonId
            localPreferences.setStringSet(
                key = KEY_UNLOCKED_PREMIUMS,
                value = newUnlocked.map { it.toString() }.toSet(),
            )
            val unlockedEmoticon = _uiState.value.emoticons.find { it.id == emoticonId }
            _uiState.update { state ->
                state.copy(
                    unlockedPremiumIds = newUnlocked,
                    selectedEmoticon = unlockedEmoticon ?: state.selectedEmoticon,
                )
            }
        }
    }

    private fun handleTextAlignToggle() {
        _uiState.update { state ->
            val nextAlign = when (state.textAlign) {
                TextAlign.Start -> TextAlign.Center
                TextAlign.Center -> TextAlign.End
                else -> TextAlign.Start
            }
            state.copy(textAlign = nextAlign)
        }
    }

    private fun TextAlign.toDbString(): String = when (this) {
        TextAlign.Center -> "center"
        TextAlign.End -> "end"
        else -> "start"
    }

    private fun String.toTextAlign(): TextAlign = when (this) {
        "center" -> TextAlign.Center
        "end" -> TextAlign.End
        else -> TextAlign.Start
    }

    private fun serializePhotoUris(photoUris: List<String>): String? {
        if (photoUris.isEmpty()) return null
        return photoUris.joinToString(SEPARATOR)
    }

    private fun parsePhotoUris(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        return if (value.contains(SEPARATOR)) {
            value.split(SEPARATOR).filter { it.isNotBlank() }
        } else {
            listOf(value)
        }
    }

    private companion object {
        const val SEPARATOR = "||"
        const val KEY_UNLOCKED_PREMIUMS = "unlocked_premium_emoticon_ids"
    }
}
