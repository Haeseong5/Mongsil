package com.cashproject.mongsil.database


import com.cashproject.mongsil.database.dao.DiaryDao
import com.cashproject.mongsil.database.model.CommentEntity

import kotlinx.coroutines.flow.Flow

class DiaryDataSource(
    private val diaryDao: DiaryDao = AppDatabase.getInstance().diaryDao(),
) {
    fun getAllComments(): Flow<List<CommentEntity>> {
        return diaryDao.getAllComments()
    }

    suspend fun insertComment(commentEntity: CommentEntity) {
        return diaryDao.insert(commentEntity)
    }

    suspend fun deleteComment(id: Int) {
        return diaryDao.delete(id)
    }
}