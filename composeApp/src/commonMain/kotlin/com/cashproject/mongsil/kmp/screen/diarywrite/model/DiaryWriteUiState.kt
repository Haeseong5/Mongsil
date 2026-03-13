package com.cashproject.mongsil.kmp.screen.diarywrite.model

import androidx.compose.ui.text.style.TextAlign
import com.cashproject.mongsil.kmp.model.Emoticon

/**
 * 일기 작성 화면의 UI 상태를 나타냅니다.
 *
 * @property year 선택된 년도
 * @property month 선택된 월
 * @property day 선택된 일
 * @property content 작성 중인 일기 내용
 * @property selectedEmoticon 선택된 이모티콘
 * @property emoticons 이모티콘 목록
 * @property showEmoticonBottomSheet 이모티콘 바텀시트 표시 여부
 * @property isLoading 삭제 처리 중 여부
 * @property isSaving 자동 저장 중 여부
 */
data class DiaryWriteUiState(
    val year: Int = 2025,
    val month: Int = 1,
    val day: Int = 1,
    val content: String = "",
    val selectedEmoticon: Emoticon? = null,
    val photoUris: List<String> = emptyList(),
    val emoticons: List<Emoticon> = emptyList(),
    val showEmoticonBottomSheet: Boolean = false,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isInitializing: Boolean = true,
    val showDeleteDialog: Boolean = false,
    val isExistingDiary: Boolean = false,
    val savedContent: String = "",
    val savedPhotoUris: List<String> = emptyList(),
    val savedEmoticonId: Int? = null,
    val unlockedPremiumIds: Set<Int> = emptySet(),
    val textAlign: TextAlign = TextAlign.Start,
    val savedTextAlign: TextAlign = TextAlign.Start,
) {
    val hasContent: Boolean
        get() = content.isNotBlank() || photoUris.isNotEmpty()

    val hasUnsavedChanges: Boolean
        get() = content != savedContent
                || photoUris != savedPhotoUris
                || selectedEmoticon?.id != savedEmoticonId
                || textAlign != savedTextAlign
}
