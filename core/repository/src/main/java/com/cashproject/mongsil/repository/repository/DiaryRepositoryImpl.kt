package com.cashproject.mongsil.repository.repository

import com.cashproject.mongsil.common.extensions.excludeTimeFromDate
import com.cashproject.mongsil.database.DiaryDataSource
import com.cashproject.mongsil.repository.model.CommentModel
import com.cashproject.mongsil.repository.model.DailyEmoticon
import com.cashproject.mongsil.repository.model.toDailyEmoticons
import com.cashproject.mongsil.repository.model.toDomain
import com.cashproject.mongsil.repository.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class DiaryRepositoryImpl(
    private val diaryService: DiaryDataSource = DiaryDataSource()
) : DiaryRepository {
    override fun getAllComments(): Flow<List<CommentModel>> {
        return diaryService.getAllComments().map { it.toDomain() }
    }

    override fun loadCommentListByDate(date: Date): Flow<List<CommentModel>> {
        return diaryService.getAllComments()
            .map {
                it.toDomain()
                    .filter { comment ->
                        (comment.date.excludeTimeFromDate() == date.excludeTimeFromDate())
                    }
            }
    }

    override suspend fun insert(comment: CommentModel) {
        diaryService.insertComment(comment.toEntity())
    }

    override suspend fun delete(id: Int) {
        diaryService.deleteComment(id)
    }

    override fun loadDailyEmotions(): Flow<List<DailyEmoticon>> {
        return diaryService.getAllComments().map { it.toDailyEmoticons() }
    }
}