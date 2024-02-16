package com.cashproject.mongsil.repository

import com.cashproject.mongsil.data.repository.DiaryRepository
import com.cashproject.mongsil.database.DiaryDataSource
import com.cashproject.mongsil.extension.excludeTimeFromDate
import com.cashproject.mongsil.repository.model.DailyEmoticon
import com.cashproject.mongsil.repository.model.toDailyEmoticons
import com.cashproject.mongsil.ui.pages.diary.Comment
import com.cashproject.mongsil.ui.pages.diary.toDomain
import com.cashproject.mongsil.ui.pages.diary.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class DiaryRepositoryImpl(
    private val diaryService: DiaryDataSource = DiaryDataSource()
): DiaryRepository {
    override fun getAllComments(): Flow<List<Comment>> {
        return diaryService.getAllComments().map { it.toDomain() }
    }

    override fun loadCommentListByDate(date: Date): Flow<List<Comment>> {
        return diaryService.getAllComments()
            .map {
                it.toDomain()
                    .filter { comment ->
                        (comment.date.excludeTimeFromDate() == date.excludeTimeFromDate())
                    }
            }
    }

    override suspend fun insert(comment: Comment) {
        diaryService.insertComment(comment.toEntity())
    }

    override suspend fun delete(id: Int) {
        diaryService.deleteComment(id)
    }

    override fun loadDailyEmotions(): Flow<List<DailyEmoticon>> {
        return diaryService.getAllComments().map { it.toDailyEmoticons() }
    }
}