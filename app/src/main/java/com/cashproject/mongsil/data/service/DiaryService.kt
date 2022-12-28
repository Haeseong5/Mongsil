package com.cashproject.mongsil.data.service

import com.cashproject.mongsil.data.db.AppDatabase
import com.cashproject.mongsil.data.db.dao.DiaryDao
import com.cashproject.mongsil.data.db.entity.CommentEntity

class DiaryService(
    private val diaryDao: DiaryDao = AppDatabase.getInstance().diaryDao(),
) {
    suspend fun getAllComments(): List<CommentEntity> {
        return diaryDao.getAllComments()
    }

    suspend fun insertComment(commentEntity: CommentEntity) {
        return diaryDao.insert(commentEntity)
    }

    suspend fun deleteComment(id: Int) {
        return diaryDao.delete(id)
    }
}