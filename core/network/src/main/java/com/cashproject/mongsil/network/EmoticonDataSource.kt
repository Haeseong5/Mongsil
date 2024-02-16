package com.cashproject.mongsil.network

import com.cashproject.mongsil.network.model.EmoticonResponse
import com.cashproject.mongsil.network.retrofit.EmoticonApi
import kotlinx.serialization.ExperimentalSerializationApi

object EmoticonDataSource : EmoticonApi {
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getEmoticons(): List<EmoticonResponse> {
        return ApiProvider.of(EmoticonApi::class).getEmoticons()
//        return createMockEmoticons()  // for test
    }

    private fun createMockEmoticons(): List<EmoticonResponse> {

        val list = mutableListOf<EmoticonResponse>()
        for (i in 1..100) {
            list.add(
                EmoticonResponse(
                    id = i,
                    imageUrl = "https://firebasestorage.googleapis.com/v0/b/mongsil-8dc44.appspot.com/o/emoticons%2Femoticon_01.png?alt=media&token=a58f5622-6568-49a4-9484-90d5cce02316",
                    title = "행복$i",
                    textColor = "#dcc75a",//            R.color.emoticon_01_happy_text,
                    backgroundColor = "#fff9da",//            R.color.emoticon_01_happy_background
                )
            )
        }
        return list
    }
}