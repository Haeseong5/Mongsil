package com.cashproject.mongsil.kmp.model

import androidx.compose.ui.graphics.Color

/**
 * 몽실 감정 타입
 * 각 날짜에 표시될 수 있는 감정 상태를 정의
 */
enum class MongsilMood(
    val emoji: String,
    val backgroundColor: Color,
    val displayName: String
) {
    VERY_HAPPY(
        emoji = "😊",
        backgroundColor = Color(0xFFFFF9C4), // 연한 노랑
        displayName = "매우 행복"
    ),
    HAPPY(
        emoji = "🙂",
        backgroundColor = Color(0xFFFFF59D), // 노랑
        displayName = "행복"
    ),
    EXCITED(
        emoji = "😆",
        backgroundColor = Color(0xFFFFCC80), // 주황
        displayName = "신남"
    ),
    LOVELY(
        emoji = "🥰",
        backgroundColor = Color(0xFFF8BBD0), // 분홍
        displayName = "사랑스러움"
    ),
    PEACEFUL(
        emoji = "😌",
        backgroundColor = Color(0xFFA5D6A7), // 민트
        displayName = "평온함"
    ),
    NORMAL(
        emoji = "😐",
        backgroundColor = Color(0xFFE0E0E0), // 회색
        displayName = "보통"
    ),
    TIRED(
        emoji = "😴",
        backgroundColor = Color(0xFFB0BEC5), // 푸른 회색
        displayName = "피곤함"
    ),
    SAD(
        emoji = "😢",
        backgroundColor = Color(0xFF90CAF9), // 파랑
        displayName = "슬픔"
    ),
    ANGRY(
        emoji = "😠",
        backgroundColor = Color(0xFFEF9A9A), // 빨강
        displayName = "화남"
    ),
    ANXIOUS(
        emoji = "😰",
        backgroundColor = Color(0xFFCE93D8), // 보라
        displayName = "불안함"
    );

    companion object {
        /**
         * 테스트용 샘플 데이터
         */
        fun getSampleMoods(): Map<Int, MongsilMood> {
            return mapOf(
                1 to VERY_HAPPY,
                2 to HAPPY,
                3 to EXCITED,
                6 to LOVELY,
                9 to PEACEFUL,
                15 to NORMAL,
                20 to TIRED,
                25 to SAD
            )
        }
    }
}
