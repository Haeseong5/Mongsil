package com.cashproject.mongsil.kmp

import kotlinx.serialization.Serializable

/**
 * 앱의 모든 네비게이션 경로를 정의하는 sealed interface
 * @Serializable로 type-safe한 navigation을 구현
 * 
 * 장점:
 * 1. Type-safe: 컴파일 타임에 route 검증
 * 2. 자동 직렬화: argument 전달이 자동으로 처리됨
 * 3. IDE 지원: 자동완성과 리팩토링 지원
 */
sealed interface Route {
    /**
     * 홈 화면 (캘린더)
     */
    @Serializable
    data object Home : Route

    /**
     * 캘린더 상세 화면 (현재는 Home과 동일)
     */
    @Serializable
    data object Calendar : Route

    /**
     * 카운터 화면
     */
    @Serializable
    data object Counter : Route

    // 파라미터가 필요한 화면의 예시:
    // @Serializable
    // data class DiaryDetail(val diaryId: Long, val date: String) : Route
    
    // 선택적 파라미터 예시:
    // @Serializable
    // data class Settings(val section: String? = null) : Route
}