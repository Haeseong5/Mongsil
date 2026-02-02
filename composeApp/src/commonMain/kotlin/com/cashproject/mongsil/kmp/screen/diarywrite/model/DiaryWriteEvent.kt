package com.cashproject.mongsil.kmp.screen.diarywrite.model

/**
 * 일기 작성 화면에서 발생하는 사용자 이벤트를 정의합니다.
 */
sealed interface DiaryWriteEvent {
    /**
     * 일기 내용이 변경되었을 때
     */
    data class OnContentChange(val content: String) : DiaryWriteEvent
    
    /**
     * 저장 버튼을 눌렀을 때
     */
    data object OnSaveClick : DiaryWriteEvent
    
    /**
     * 뒤로가기 버튼을 눌렀을 때
     */
    data object OnBackClick : DiaryWriteEvent
    
    /**
     * 시스템 뒤로가기를 눌렀을 때
     */
    data object OnBackPressed : DiaryWriteEvent
    
    /**
     * 종료 확인 다이얼로그에서 확인 버튼을 눌렀을 때
     */
    data object OnExitConfirm : DiaryWriteEvent
    
    /**
     * 종료 확인 다이얼로그에서 취소 버튼을 눌렀을 때
     */
    data object OnExitCancel : DiaryWriteEvent
}
