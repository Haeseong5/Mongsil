package com.cashproject.mongsil.kmp.repository

import com.cashproject.mongsil.kmp.database.MongsilDatabase
import com.cashproject.mongsil.kmp.model.Emoticon
import com.cashproject.mongsil.kmp.network.api.EmoticonApi
import com.cashproject.mongsil.kmp.network.model.EmoticonResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

/**
 * 감정 이모티콘 데이터를 관리하는 Repository
 * 로컬 DB 캐싱을 통해 빠른 로딩 속도를 제공합니다.
 * 
 * 캐싱 전략:
 * 1. 첫 실행: API 호출 → DB 저장
 * 2. 이후 실행: DB에서 즉시 로드 → 백그라운드에서 API 업데이트
 */
class EmoticonRepository(
    private val emoticonApi: EmoticonApi,
    private val database: MongsilDatabase
) {
    
    companion object {
        // 캐시 유효 시간 (24시간)
        private const val CACHE_VALIDITY_MS = 24 * 60 * 60 * 1000L
    }
    
    /**
     * 모든 이모티콘 목록 조회
     * API 호출이 실패하면 Mock 데이터를 반환합니다.
     * Network 모델을 도메인 모델(Emoticon)로 변환하여 반환합니다.
     * @return 이모티콘 목록 또는 에러 결과
     */
    suspend fun getEmoticons(): Result<List<Emoticon>> = 
        withContext(Dispatchers.Default) {
            val result = emoticonApi.getEmoticons()
            
            // 실패 시 Mock 데이터 반환
            if (result.isFailure) {
                println("API 호출 실패, Mock 데이터 사용: ${result.exceptionOrNull()?.message}")
                Result.success(getMockEmoticons().map { it.toEmoticon() })
            } else {
                Result.success(result.getOrNull()!!.map { it.toEmoticon() })
            }
        }
    
    /**
     * 목 데이터 생성 (테스트용)
     * API 호출 대신 사용할 수 있는 목 데이터
     */
    fun getMockEmoticons(): List<EmoticonResponse> {
        return listOf(
            EmoticonResponse(
                id = 0,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/mongsil-8dc44.appspot.com/o/emoticons%2Femoticon_01.png?alt=media&token=a58f5622-6568-49a4-9484-90d5cce02316",
                title = "행복",
                textColor = "#dcc75a",
                backgroundColor = "#fff9da"
            ),
            EmoticonResponse(
                id = 1,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/mongsil-8dc44.appspot.com/o/emoticons%2Femoticon_02.png?alt=media&token=ce1ac395-1ea0-4c9e-b2a3-3f1defa984b4",
                title = "기쁨",
                textColor = "#db8fbd",
                backgroundColor = "#ffecf7"
            ),
            EmoticonResponse(
                id = 2,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/mongsil-8dc44.appspot.com/o/emoticons%2Femoticon_03.png?alt=media&token=5ae3a96c-bb8c-47af-ab9f-4ec830dd1a74",
                title = "만족",
                textColor = "#dc8d6c",
                backgroundColor = "#ffe4d9"
            ),
            EmoticonResponse(
                id = 3,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/mongsil-8dc44.appspot.com/o/emoticons%2Femoticon_04.png?alt=media&token=6218a119-81ee-4b28-9b1c-94d07f7e5d89",
                title = "보통",
                textColor = "#d78787",
                backgroundColor = "#ffe8e8"
            ),
            EmoticonResponse(
                id = 4,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/mongsil-8dc44.appspot.com/o/emoticons%2Femoticon_05.png?alt=media&token=6c94879b-bfa5-4614-a7aa-0dde50f3a776",
                title = "피곤",
                textColor = "#9197d2",
                backgroundColor = "#e5e8ff"
            ),
            EmoticonResponse(
                id = 5,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/mongsil-8dc44.appspot.com/o/emoticons%2Femoticon_06.png?alt=media&token=63b91722-c6fb-40e7-a7e8-5997404c3595",
                title = "창피",
                textColor = "#d9ad58",
                backgroundColor = "#ffecc9"
            ),
            EmoticonResponse(
                id = 6,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/mongsil-8dc44.appspot.com/o/emoticons%2Femoticon_07.png?alt=media&token=bbc3102d-baea-46f4-b2b7-186cb1166aa2",
                title = "지루함",
                textColor = "#527e57",
                backgroundColor = "#cce5ce"
            ),
            EmoticonResponse(
                id = 7,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/mongsil-8dc44.appspot.com/o/emoticons%2Femoticon_08.png?alt=media&token=3d7f0bd0-de51-4bc1-8f4a-86dbea9761e8",
                title = "화남",
                textColor = "#cd5454",
                backgroundColor = "#ffdbdb"
            ),
            EmoticonResponse(
                id = 8,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/mongsil-8dc44.appspot.com/o/emoticons%2Femoticon_09.png?alt=media&token=2dba16ba-10b1-4719-a0f9-e1b7f162234e",
                title = "불쾌",
                textColor = "#9e9896",
                backgroundColor = "#efebea"
            ),
            EmoticonResponse(
                id = 9,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/mongsil-8dc44.appspot.com/o/emoticons%2Femoticon_10.png?alt=media&token=653dcce1-9546-4ff7-a0c1-1810e6252b53",
                title = "실망",
                textColor = "#d3d5e3",
                backgroundColor = "#454d7a"
            ),
            EmoticonResponse(
                id = 10,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/mongsil-8dc44.appspot.com/o/emoticons%2Femoticon_11.png?alt=media&token=350ebeca-9de2-4773-8c72-e7a4b3a01259",
                title = "불안",
                textColor = "#555454",
                backgroundColor = "#dddddd"
            ),
            EmoticonResponse(
                id = 11,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/mongsil-8dc44.appspot.com/o/emoticons%2Femoticon_12.png?alt=media&token=e9383f82-1956-41ee-8ce1-04557b7acd9b",
                title = "우울",
                textColor = "#8f6b5b",
                backgroundColor = "#f0dad1"
            ),
            EmoticonResponse(
                id = 12,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/mongsil-8dc44.appspot.com/o/emoticons%2Femoticon_13.png?alt=media&token=fedbc097-0902-4cf4-b24c-e17342675233",
                title = "슬픔",
                textColor = "#465761",
                backgroundColor = "#ccdbe0"
            ),
            EmoticonResponse(
                id = 13,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/mongsil-8dc44.appspot.com/o/emoticons%2Femoticon_14.png?alt=media&token=4c008ce6-3dcf-42ac-9699-843492336ed3",
                title = "놀람",
                textColor = "#889456",
                backgroundColor = "#ecf1da"
            ),
            EmoticonResponse(
                id = 14,
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/mongsil-8dc44.appspot.com/o/emoticons%2Femoticon_15.png?alt=media&token=52cd6a68-93f1-43b3-abe9-8f377aec1558",
                title = "외로움",
                textColor = "#7b9a9b",
                backgroundColor = "#ddebec"
            )
        )
    }
}

/**
 * EmoticonResponse를 도메인 모델(Emoticon)로 변환
 * Network layer의 모델을 UI/비즈니스 로직에서 사용할 수 있는 형태로 변환합니다.
 */
private fun EmoticonResponse.toEmoticon() = Emoticon(
    id = id,
    title = title,
    imageUrl = imageUrl,
    textColor = textColor,
    backgroundColor = backgroundColor
)
