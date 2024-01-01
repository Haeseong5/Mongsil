package com.cashproject.mongsil.data.db.dao

import androidx.room.*
import com.cashproject.mongsil.data.db.entity.CommentEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface DiaryDao {

    @Query("SELECT * FROM Comment ORDER BY time ASC")
    suspend fun getAllComments(): List<CommentEntity>

    @Query("SELECT * FROM Comment WHERE strftime('%Y-%m-%d', date/1000, 'unixepoch') = strftime('%Y-%m-%d', :date/1000, 'unixepoch')")
    fun loadCommentListByDate(date: Long): Flow<List<CommentEntity>>

    @Insert
    suspend fun insert(commentEntity: CommentEntity)

    @Query("DELETE FROM Comment WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM Comment ORDER BY time DESC LIMIT 1")
    fun loadLastCommentsByDate(): Flow<List<CommentEntity>>
}