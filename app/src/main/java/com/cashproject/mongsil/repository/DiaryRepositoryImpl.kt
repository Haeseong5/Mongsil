package com.cashproject.mongsil.repository

import com.cashproject.mongsil.data.db.entity.CommentEntity
import com.cashproject.mongsil.data.repository.DiaryRepository
import com.cashproject.mongsil.data.service.DiaryService
import com.cashproject.mongsil.ui.pages.detail.Comment
import com.cashproject.mongsil.ui.pages.detail.toDomain
import com.cashproject.mongsil.ui.pages.detail.toEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date
import kotlin.time.Duration.Companion.milliseconds

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
}