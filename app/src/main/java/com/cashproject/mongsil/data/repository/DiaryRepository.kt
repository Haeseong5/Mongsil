package com.cashproject.mongsil.data.repository

import com.cashproject.mongsil.repository.model.DailyEmoticon
import com.cashproject.mongsil.ui.pages.diary.Comment
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface DiaryRepository {

    fun getAllComments(): Flow<List<Comment>>

    fun loadCommentListByDate(date: Date): Flow<List<Comment>>

    suspend fun insert(comment: Comment)

    suspend fun delete(id: Int)

    fun loadDailyEmotions(): Flow<List<DailyEmoticon>>
}