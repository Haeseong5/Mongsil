package com.cashproject.mongsil.kmp.screen.diarywrite

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.data.DiaryRepository
import com.cashproject.mongsil.kmp.core.data.EmoticonRepository
import com.cashproject.mongsil.kmp.core.datastore.LocalPreferences
import com.cashproject.mongsil.kmp.firebase.FirebaseService
import com.cashproject.mongsil.kmp.screen.diarywrite.model.DiaryWriteEvent
import com.cashproject.mongsil.kmp.screen.diarywrite.model.DiaryWriteSideEffect
import com.cashproject.mongsil.kmp.screen.diarywrite.model.DiaryWriteUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

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
    private val firebaseService: FirebaseService,
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

    private var autoSaveJob: Job? = null

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
            val loadedTextColor = diary?.textColor?.hexToColor() ?: Color.Black

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
                    textColor = loadedTextColor,
                    savedTextColor = loadedTextColor,
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
            is DiaryWriteEvent.OnBackClick -> handleBackClick()
            is DiaryWriteEvent.OnBackPressed -> handleBackPressed()
            is DiaryWriteEvent.OnDeleteClick -> handleDeleteClick()
            is DiaryWriteEvent.OnDeleteConfirm -> handleDeleteConfirm()
            is DiaryWriteEvent.OnDeleteCancel -> handleDeleteCancel()
            is DiaryWriteEvent.OnPremiumEmoticonClick -> handlePremiumEmoticonClick(event.emoticon)
            is DiaryWriteEvent.OnAdRewardEarned -> handleAdRewardEarned(event.emoticonId)
            is DiaryWriteEvent.OnAdDismissed -> Unit
            is DiaryWriteEvent.OnTextAlignToggle -> handleTextAlignToggle()
            is DiaryWriteEvent.OnColorPickerToggle -> handleColorPickerToggle()
            is DiaryWriteEvent.OnTextColorSelected -> handleTextColorSelected(event.color)
            is DiaryWriteEvent.OnInsertCurrentTime -> handleInsertCurrentTime()
        }
    }

    private fun handleContentChange(content: String) {
        _uiState.update { it.copy(content = content) }
        scheduleAutoSave()
    }

    private fun handleEmoticonButtonClick() {
        firebaseService.logEvent(
            EVENT_TOOLBAR_CLICKED,
            mapOf(PARAM_TOOLBAR_ACTION to ACTION_EMOTICON)
        )
        _uiState.update { it.copy(showEmoticonBottomSheet = true) }
    }

    private fun handleEmoticonSelected(emoticon: com.cashproject.mongsil.kmp.model.Emoticon) {
        firebaseService.logEvent(
            name = EVENT_EMOTICON_SELECTED,
            params = mapOf(
                PARAM_EMOTICON_ID to emoticon.id.toString(),
                PARAM_EMOTICON_TITLE to emoticon.title,
                PARAM_EMOTICON_IS_PREMIUM to emoticon.isPremium.toString(),
            )
        )
        _uiState.update {
            it.copy(
                selectedEmoticon = emoticon,
                showEmoticonBottomSheet = false
            )
        }
        scheduleAutoSave()
    }

    private fun handleEmoticonBottomSheetDismiss() {
        _uiState.update { it.copy(showEmoticonBottomSheet = false) }
    }

    private fun handlePhotosSelected(photoUris: List<String>) {
        if (photoUris.isEmpty()) return
        firebaseService.logEvent(EVENT_TOOLBAR_CLICKED, mapOf(PARAM_TOOLBAR_ACTION to ACTION_PHOTO))
        _uiState.update { state -> state.copy(photoUris = state.photoUris + photoUris) }
        scheduleAutoSave()
    }

    private fun handlePhotoRemoved(index: Int) {
        _uiState.update { state ->
            if (index !in state.photoUris.indices) return@update state
            state.copy(photoUris = state.photoUris.filterIndexed { i, _ -> i != index })
        }
        scheduleAutoSave()
    }

    private fun handleTextAlignToggle() {
        firebaseService.logEvent(
            EVENT_TOOLBAR_CLICKED,
            mapOf(PARAM_TOOLBAR_ACTION to ACTION_TEXT_ALIGN)
        )
        _uiState.update { state ->
            val nextAlign = when (state.textAlign) {
                TextAlign.Start -> TextAlign.Center
                TextAlign.Center -> TextAlign.End
                else -> TextAlign.Start
            }
            state.copy(textAlign = nextAlign)
        }
        scheduleAutoSave()
    }

    private fun handleColorPickerToggle() {
        if (!_uiState.value.showColorPalette) {
            firebaseService.logEvent(
                EVENT_TOOLBAR_CLICKED,
                mapOf(PARAM_TOOLBAR_ACTION to ACTION_COLOR_PICKER)
            )
        }
        _uiState.update { it.copy(showColorPalette = !it.showColorPalette) }
    }

    private fun handleTextColorSelected(color: Color) {
        _uiState.update { it.copy(textColor = color) }
        scheduleAutoSave()
    }

    @OptIn(ExperimentalTime::class)
    private fun handleInsertCurrentTime() {
        firebaseService.logEvent(
            EVENT_TOOLBAR_CLICKED,
            mapOf(PARAM_TOOLBAR_ACTION to ACTION_INSERT_TIME)
        )
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val isAm = now.hour < 12
        val displayHour = when {
            now.hour == 0 -> 12
            now.hour > 12 -> now.hour - 12
            else -> now.hour
        }
        val timeText = "${if (isAm) "AM" else "PM"} ${
            displayHour.toString().padStart(2, '0')
        }:${now.minute.toString().padStart(2, '0')}" + "\n"
        _uiState.update { state ->
            val newContent =
                if (state.content.isEmpty()) timeText else "${state.content}\n$timeText"
            state.copy(content = newContent)
        }
        scheduleAutoSave()
    }

    private fun handleBackClick() {
        autoSaveJob?.cancel()
        viewModelScope.launch {
            if (_uiState.value.hasUnsavedChanges && _uiState.value.hasContent) {
                performSave()
            }
            _sideEffect.send(DiaryWriteSideEffect.OnBack)
        }
    }

    private fun handleBackPressed() {
        autoSaveJob?.cancel()
        viewModelScope.launch {
            if (_uiState.value.hasUnsavedChanges && _uiState.value.hasContent) {
                performSave()
            }
            _sideEffect.send(DiaryWriteSideEffect.OnBack)
        }
    }

    private fun handleDeleteClick() {
        _uiState.update { it.copy(showDeleteDialog = true) }
    }

    private fun handleDeleteConfirm() {
        val state = _uiState.value
        autoSaveJob?.cancel()
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

    private fun scheduleAutoSave() {
        if (!_uiState.value.hasContent) return
        autoSaveJob?.cancel()
        autoSaveJob = viewModelScope.launch {
            delay(AUTO_SAVE_DELAY_MS)
            performSave()
        }
    }

    private suspend fun performSave() {
        val state = _uiState.value
        if (!state.hasContent || !state.hasUnsavedChanges) return

        _uiState.update { it.copy(isSaving = true) }

        val result = diaryRepository.saveDiary(
            year = state.year,
            month = state.month,
            day = state.day,
            content = state.content,
            emoticonId = state.selectedEmoticon?.id?.toLong(),
            photoUri = serializePhotoUris(state.photoUris),
            textAlign = state.textAlign.toDbString(),
            textColor = state.textColor.toHexString(),
        )

        result.fold(
            onSuccess = {
                if (!state.isExistingDiary) {
                    firebaseService.logEvent(
                        name = EVENT_DIARY_CREATED,
                        params = mapOf(
                            PARAM_HAS_PHOTO to (state.photoUris.isNotEmpty()).toString(),
                            PARAM_EMOTICON_ID to (state.selectedEmoticon?.id?.toString() ?: "none"),
                        )
                    )
                }
                _uiState.update { s ->
                    s.copy(
                        isSaving = false,
                        isExistingDiary = true,
                        savedContent = s.content,
                        savedPhotoUris = s.photoUris,
                        savedEmoticonId = s.selectedEmoticon?.id,
                        savedTextAlign = s.textAlign,
                        savedTextColor = s.textColor,
                    )
                }
            },
            onFailure = {
                _uiState.update { it.copy(isSaving = false) }
            }
        )
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

    private fun Color.toHexString(): String {
        val argb = toArgb()
        return buildString {
            for (shift in 24 downTo 0 step 8) {
                append(((argb ushr shift) and 0xFF).toString(16).padStart(2, '0'))
            }
        }.uppercase()
    }

    private fun String.hexToColor(): Color = try {
        Color(toLong(16).toInt())
    } catch (e: NumberFormatException) {
        Color.Black
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
        const val AUTO_SAVE_DELAY_MS = 1500L
        const val EVENT_DIARY_CREATED = "diary_created"
        const val PARAM_HAS_PHOTO = "has_photo"
        const val PARAM_EMOTICON_ID = "emoticon_id"

        const val EVENT_TOOLBAR_CLICKED = "diary_toolbar_clicked"
        const val PARAM_TOOLBAR_ACTION = "action"
        const val ACTION_EMOTICON = "emoticon"
        const val ACTION_PHOTO = "photo"
        const val ACTION_TEXT_ALIGN = "text_align"
        const val ACTION_COLOR_PICKER = "color_picker"
        const val ACTION_INSERT_TIME = "insert_time"

        const val EVENT_EMOTICON_SELECTED = "emoticon_selected"
        const val PARAM_EMOTICON_TITLE = "emoticon_title"
        const val PARAM_EMOTICON_IS_PREMIUM = "is_premium"
    }
}
