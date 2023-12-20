package com.cashproject.mongsil.data.service

import com.cashproject.mongsil.data.db.AppDatabase
import com.cashproject.mongsil.data.db.dao.DiaryDao
import com.cashproject.mongsil.data.db.entity.CommentEntity
import com.cashproject.mongsil.ui.pages.detail.Comment
import com.cashproject.mongsil.ui.pages.detail.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class DiaryService(
    private val diaryDao: DiaryDao = AppDatabase.getInstance().diaryDao(),
) {
    suspend fun getAllComments(): List<CommentEntity> {
        return diaryDao.getAllComments()
    }

    fun loadCommentsByDate(date: Date): Flow<List<Comment>> {
        return diaryDao.loadCommentListByDate(date.time)
            .map { it.toDomain() }
    }

    suspend fun insertComment(commentEntity: CommentEntity) {
        return diaryDao.insert(commentEntity)
    }

    suspend fun deleteComment(id: Int) {
        return diaryDao.delete(id)
    }
}