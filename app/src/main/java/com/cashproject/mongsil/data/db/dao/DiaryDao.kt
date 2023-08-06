package com.cashproject.mongsil.data.db.dao

import androidx.room.*
import com.cashproject.mongsil.data.db.entity.CommentEntity

@Dao
interface DiaryDao {

    //TODO 월별/일별 조회 쿼리
    @Query("SELECT * FROM Comment WHERE docId = :docId ORDER BY time ASC")
    suspend fun getAllComments(docId: String): List<CommentEntity>

    @Query("SELECT * FROM Comment")
    suspend fun getAllComments() : List<CommentEntity>

    @Insert
    suspend fun insert(commentEntity: CommentEntity)

    @Query("DELETE FROM Comment WHERE id = :id")
    suspend fun delete(id: Int)

}