package com.cashproject.mongsil.kmp.screen.diarywrite.model

import com.cashproject.mongsil.kmp.model.Emoticon

/**
 * 일기 작성 화면에서 발생하는 사용자 이벤트를 정의합니다.
 */
sealed interface DiaryWriteEvent {
    /**
     * 일기 내용이 변경되었을 때
     */
    data class OnContentChange(val content: String) : DiaryWriteEvent
    
    /**
     * 이모티콘 추가 버튼을 눌렀을 때
     */
    data object OnEmoticonButtonClick : DiaryWriteEvent
    
    /**
     * 이모티콘을 선택했을 때
     */
    data class OnEmoticonSelected(val emoticon: Emoticon) : DiaryWriteEvent

    /**
     * 사진이 선택되었을 때
     */
    data class OnPhotosSelected(val photoUris: List<String>) : DiaryWriteEvent

    /**
     * 첨부한 사진 삭제
     */
    data class OnPhotoRemoved(val index: Int) : DiaryWriteEvent
    
    /**
     * 이모티콘 바텀시트를 닫았을 때
     */
    data object OnEmoticonBottomSheetDismiss : DiaryWriteEvent
    
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

    /**
     * 삭제 버튼을 눌렀을 때
     */
    data object OnDeleteClick : DiaryWriteEvent

    /**
     * 삭제 확인 다이얼로그에서 확인 버튼을 눌렀을 때
     */
    data object OnDeleteConfirm : DiaryWriteEvent

    /**
     * 삭제 확인 다이얼로그에서 취소 버튼을 눌렀을 때
     */
    data object OnDeleteCancel : DiaryWriteEvent

    /**
     * 잠긴 프리미엄 이모티콘을 클릭했을 때
     */
    data class OnPremiumEmoticonClick(val emoticon: Emoticon) : DiaryWriteEvent

    /**
     * 광고 시청 완료 후 보상 지급
     */
    data class OnAdRewardEarned(val emoticonId: Int) : DiaryWriteEvent

    /**
     * 광고가 닫혔을 때 (보상 없이)
     */
    data object OnAdDismissed : DiaryWriteEvent

    /**
     * 텍스트 정렬 토글 버튼을 눌렀을 때
     */
    data object OnTextAlignToggle : DiaryWriteEvent
}
