package com.cashproject.mongsil.data.repository

import androidx.room.Insert
import androidx.room.Query
import com.cashproject.mongsil.data.db.entity.CommentEntity
import com.cashproject.mongsil.ui.pages.detail.Comment
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface DiaryRepository {

    suspend fun getAllComments(): List<Comment>

    fun loadCommentListByDate(date: Date): Flow<List<Comment>>

    suspend fun insert(comment: Comment)

    suspend fun delete(id: Int)

}