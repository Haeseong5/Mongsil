package com.cashproject.mongsil.kmp.network.api

import com.cashproject.mongsil.kmp.network.model.EmoticonResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/**
 * 감정 이모티콘 API
 * Ktor HttpClient를 사용하여 이모티콘 데이터를 가져옵니다.
 */
class EmoticonApi(private val client: HttpClient) {
    
    companion object {
        private const val BASE_URL = "https://haeseong5.github.io"
    }
    
    /**
     * 모든 이모티콘 목록 조회
     * @return 이모티콘 목록
     */
    suspend fun getEmoticons(): Result<List<EmoticonResponse>> {
        return try {
            val emoticons = client.get("$BASE_URL/api/emoticon.json")
                .body<List<EmoticonResponse>>()
            Result.success(emoticons)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
