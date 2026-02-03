package com.cashproject.mongsil.kmp.model

/**
 * 감정 이모티콘 도메인 모델
 * UI와 비즈니스 로직에서 사용되는 이모티콘 데이터
 *
 * @property id 이모티콘 ID
 * @property title 감정 제목
 * @property imageUrl 이모티콘 이미지 URL
 * @property textColor 텍스트 색상 (Hex)
 * @property backgroundColor 배경 색상 (Hex)
 */
data class Emoticon(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val textColor: String,
    val backgroundColor: String
)
