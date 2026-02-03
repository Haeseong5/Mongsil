package com.cashproject.mongsil.kmp.network.model

import kotlinx.serialization.Serializable

/**
 * 감정 이모티콘 응답 모델
 *
 * @property id 이모티콘 ID
 * @property imageUrl 이모티콘 이미지 URL
 * @property title 감정 제목
 * @property textColor 텍스트 색상 (Hex)
 * @property backgroundColor 배경 색상 (Hex)
 */
@Serializable
data class EmoticonResponse(
    val id: Int,
    val imageUrl: String,
    val title: String,
    val textColor: String,
    val backgroundColor: String
)
