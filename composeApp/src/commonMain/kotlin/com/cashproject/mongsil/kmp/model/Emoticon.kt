package com.cashproject.mongsil.kmp.model

import com.cashproject.mongsil.kmp.core.model.TextSource

/**
 * 감정 이모티콘 도메인 모델
 *
 * @property id 이모티콘 ID
 * @property title 감정 제목
 * @property image 이모티콘 이미지 리소스
 * @property textColor 텍스트 색상 (Hex)
 * @property backgroundColor 배경 색상 (Hex)
 */
data class Emoticon(
    val id: Int,
    val title: TextSource,
    val image: ImageResource,
    val textColor: String,
    val backgroundColor: String,
    val isPremium: Boolean = false,
)
