package com.cashproject.mongsil.data.db

import com.cashproject.mongsil.data.db.entity.Comment
import com.cashproject.mongsil.data.db.entity.Saying
import com.cashproject.mongsil.data.db.dao.CommentDao
import com.cashproject.mongsil.data.db.dao.LockerDao
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

class LocalDataSource(
    private val commentDao: CommentDao,
    private val lockerDao: LockerDao
) {

    fun getAllComments(): Single<List<Comment>> {
        return commentDao.getAllComments()
    }

    fun insertComment(comment: Comment): Completable {
        return commentDao.insert(comment)
    }

    fun deleteCommentById(id: Int): Completable {
        return commentDao.deleteById(id)
    }

    suspend fun getAllLikeData(): List<Saying> {
        return lockerDao.getAll()
    }

    fun findByDocId(docId: String): Maybe<Saying> {
        return lockerDao.findByDocId(docId)
    }

    fun insertLikeSaying(docId: Saying): Completable {
        return lockerDao.insert(docId)
    }

    fun deleteLikeSayingByDocId(docId: String): Completable {
        return lockerDao.delete(docId)
    }

}