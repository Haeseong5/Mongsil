package com.cashproject.mongsil.kmp.screen.diarywrite.model

/**
 * 일기 작성 화면의 일회성 이벤트(Side Effect)를 정의합니다.
 * 네비게이션, 토스트 메시지 등 한 번만 실행되어야 하는 작업에 사용됩니다.
 */
sealed interface DiaryWriteSideEffect {
    data object DeleteSuccess : DiaryWriteSideEffect
    data object OnBack : DiaryWriteSideEffect

    /** 프리미엄 이모티콘 잠금 해제를 위한 영상 광고 요청 */
    data class ShowRewardedAd(val emoticonId: Int) : DiaryWriteSideEffect

    /** 일기 저장 실패 알림 */
    data object SaveFailed : DiaryWriteSideEffect
}
