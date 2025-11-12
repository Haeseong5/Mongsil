package com.cashproject.mongsil.repository.repository

import com.cashproject.mongsil.repository.model.CommentModel
import com.cashproject.mongsil.repository.model.DailyEmoticon
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface DiaryRepository {

    fun getAllComments(): Flow<List<CommentModel>>

    fun loadCommentListByDate(date: Date): Flow<List<CommentModel>>

    suspend fun insert(comment: CommentModel)

    suspend fun delete(id: Int)

    fun loadDailyEmotions(): Flow<List<DailyEmoticon>>
}