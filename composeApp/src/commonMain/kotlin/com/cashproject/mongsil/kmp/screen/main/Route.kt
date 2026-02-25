package com.cashproject.mongsil.kmp.screen.main

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

    @Serializable
    data object Test : Route

    /**
     * 일기 작성 화면
     * @param year 선택된 년도
     * @param month 선택된 월
     * @param day 선택된 일
     */
    @Serializable
    data class DiaryWrite(
        val year: Int,
        val month: Int,
        val day: Int
    ) : Route

    /**
     * 설정 화면
     */
    @Serializable
    data object Setting : Route

    /**
     * 몽실 스토어 화면
     */
    @Serializable
    data object MongsilStore : Route

    /**
     * 테마 설정 화면
     */
    @Serializable
    data object ThemeSetting : Route

    /**
     * 글자 스타일 화면
     */
    @Serializable
    data object FontStyle : Route

    /**
     * 화면 잠금 화면
     */
    @Serializable
    data object ScreenLock : Route

    /**
     * 백업/복원 화면
     */
    @Serializable
    data object BackupRestore : Route

    /**
     * PDF 내보내기 화면
     */
    @Serializable
    data object PdfExport : Route

    /**
     * 언어 설정 화면
     */
    @Serializable
    data object LanguageSetting : Route

    /**
     * 앱 평가하기 화면
     */
    @Serializable
    data object AppReview : Route
}