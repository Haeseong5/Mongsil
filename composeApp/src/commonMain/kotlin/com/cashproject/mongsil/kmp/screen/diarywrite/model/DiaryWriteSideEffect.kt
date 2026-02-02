package com.cashproject.mongsil.kmp.screen.diarywrite.model

/**
 * 일기 작성 화면의 일회성 이벤트(Side Effect)를 정의합니다.
 * 네비게이션, 토스트 메시지 등 한 번만 실행되어야 하는 작업에 사용됩니다.
 */
sealed interface DiaryWriteSideEffect {
    /**
     * 저장 성공 후 캘린더 화면으로 이동
     */
    data object NavigateToCalendar : DiaryWriteSideEffect
    
    /**
     * 저장 없이 캘린더 화면으로 이동
     */
    data object NavigateBack : DiaryWriteSideEffect
    
    /**
     * 저장 실패 메시지 표시
     */
    data class ShowError(val message: String) : DiaryWriteSideEffect
    
    /**
     * 저장 성공 메시지 표시
     */
    data object ShowSaveSuccess : DiaryWriteSideEffect
}
