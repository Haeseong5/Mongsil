package com.cashproject.mongsil.data.db.dao

import androidx.room.*
import com.cashproject.mongsil.data.db.entity.CommentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {

    @Query("SELECT * FROM Comment ORDER BY time ASC")
    fun getAllComments(): Flow<List<CommentEntity>>

    @Insert
    suspend fun insert(commentEntity: CommentEntity)

    @Query("DELETE FROM Comment WHERE id = :id")
    suspend fun delete(id: Int)
}