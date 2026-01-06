package com.cashproject.mongsil.kmp.network.model

import kotlinx.serialization.Serializable

/**
 * 샘플 API 응답 모델 (JSONPlaceholder)
 */
@Serializable
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)
