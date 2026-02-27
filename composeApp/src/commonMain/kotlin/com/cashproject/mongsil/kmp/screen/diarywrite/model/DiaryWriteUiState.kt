package com.cashproject.mongsil.kmp.screen.diarywrite.model

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
 * @property isLoading 저장 중 여부
 * @property showExitDialog 종료 확인 다이얼로그 표시 여부
 */
data class DiaryWriteUiState(
    val year: Int = 2025,
    val month: Int = 1,
    val day: Int = 1,
    val content: String = "",
    val selectedEmoticon: Emoticon? = null,
    val emoticons: List<Emoticon> = emptyList(),
    val showEmoticonBottomSheet: Boolean = false,
    val isLoading: Boolean = false,
    val showExitDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val isExistingDiary: Boolean = false,
) {
    /**
     * 작성 중인 내용이 있는지 확인합니다.
     */
    val hasContent: Boolean
        get() = content.isNotBlank()
}
