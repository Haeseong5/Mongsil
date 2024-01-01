package com.cashproject.mongsil.repository

import com.cashproject.mongsil.data.repository.DiaryRepository
import com.cashproject.mongsil.data.service.DiaryService
import com.cashproject.mongsil.repository.model.DailyEmoticon
import com.cashproject.mongsil.repository.model.toDailyEmoticons
import com.cashproject.mongsil.ui.pages.detail.Comment
import com.cashproject.mongsil.ui.pages.detail.toDomain
import com.cashproject.mongsil.ui.pages.detail.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class DiaryRepositoryImpl(
    private val diaryService: DiaryService = DiaryService()
): DiaryRepository {
    override suspend fun getAllComments(): List<Comment> {
        return diaryService.getAllComments().toDomain()
    }

    override fun loadCommentListByDate(date: Date): Flow<List<Comment>> {
        return diaryService.loadCommentsByDate(date)
    }

    override suspend fun insert(comment: Comment) {
        diaryService.insertComment(comment.toEntity())
    }

    override suspend fun delete(id: Int) {
        diaryService.deleteComment(id)
    }

    override fun loadDailyEmotions(): Flow<List<DailyEmoticon>> {
        return diaryService.loadLastCommentsByDate().map { it.toDailyEmoticons() }
    }
}