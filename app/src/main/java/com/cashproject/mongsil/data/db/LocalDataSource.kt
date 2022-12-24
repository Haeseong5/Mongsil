package com.cashproject.mongsil.data.db

import com.cashproject.mongsil.data.db.entity.CommentEntity
import com.cashproject.mongsil.data.db.entity.SayingEntity
import com.cashproject.mongsil.data.db.dao.CommentDao
import com.cashproject.mongsil.data.db.dao.LockerDao
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

class LocalDataSource(
    private val commentDao: CommentDao,
    private val lockerDao: LockerDao
) {

    fun getAllComments(): Single<List<CommentEntity>> {
        return commentDao.getAllComments()
    }

    fun insertComment(commentEntity: CommentEntity): Completable {
        return commentDao.insert(commentEntity)
    }

    fun deleteCommentById(id: Int): Completable {
        return commentDao.deleteById(id)
    }

    suspend fun getAllLikeData(): List<SayingEntity> {
        return lockerDao.getAll()
    }

    fun findByDocId(docId: String): Maybe<SayingEntity> {
        return lockerDao.findByDocId(docId)
    }

    fun insertLikeSaying(docId: SayingEntity): Completable {
        return lockerDao.insert(docId)
    }

    fun deleteLikeSayingByDocId(docId: String): Completable {
        return lockerDao.delete(docId)
    }

}