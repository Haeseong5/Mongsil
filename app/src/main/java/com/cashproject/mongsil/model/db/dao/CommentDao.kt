package com.cashproject.mongsil.model.db.dao

import androidx.room.*
import com.cashproject.mongsil.model.data.Comment
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CommentDao {

    @Query("SELECT * FROM Comment WHERE docId = :docId ORDER BY time ASC")
    fun getCommentsByDocId(docId: String): Single<List<Comment>>

    @Query("SELECT * FROM Comment")
    fun getAllComments() : Single<List<Comment>>

    @Insert
    fun insert(comment: Comment) : Completable

    @Query("DELETE FROM Comment WHERE id = :id")
    fun deleteById(id: Int) : Completable

    @Query("DELETE FROM Comment WHERE id = :docId")
    fun deleteByDocId(docId: String) : Completable

}