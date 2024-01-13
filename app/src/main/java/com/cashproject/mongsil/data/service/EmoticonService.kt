package com.cashproject.mongsil.data.service

import com.cashproject.mongsil.data.api.EmoticonApi
import com.cashproject.mongsil.data.api.dto.EmoticonResponse
import kotlinx.serialization.ExperimentalSerializationApi

object EmoticonService : EmoticonApi {
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getEmoticons(): List<EmoticonResponse> {
        return createMockEmoticons()
//        return ApiProvider.of(EmoticonApi::class).getEmoticons()
    }

    private fun createMockEmoticons(): List<EmoticonResponse> {

        return listOf(
            EmoticonResponse(
                id = 1,
                imageUrl = "https://avatars.githubusercontent.com/u/35673850?v=4",
                title = "행복",
//            R.drawable.emoticon_01_happy,
                textColor = "#dcc75a",//            R.color.emoticon_01_happy_text,
                backgroundColor = "#fff9da",//            R.color.emoticon_01_happy_background
            ),
            EmoticonResponse(
                id = 2,
                imageUrl = "https://avatars.githubusercontent.com/u/35673850?v=4",
                title = "불행",
//            R.drawable.emoticon_01_happy,
                textColor = "#dcc75a",//            R.color.emoticon_01_happy_text,
                backgroundColor = "#fff9da",//            R.color.emoticon_01_happy_background
            )
        )
    }
}