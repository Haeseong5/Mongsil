package com.cashproject.mongsil.model.db.datasource

import android.util.Log
import android.util.Log.d
import com.cashproject.mongsil.model.data.Comment
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.model.db.dao.CommentDao
import com.cashproject.mongsil.model.db.dao.LockerDao
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSource(private val commentDao: CommentDao, private val lockerDao: LockerDao) {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    //calendar
    fun getAllComments(): Single<List<Comment>> {
        return commentDao.getAllComments()
    }

    //comment
    fun getCommentsByDocId(docId: String): Single<List<Comment>> {
        Log.d("--getCommentsByDocId ", Thread.currentThread().name)
        return commentDao.getCommentsByDocId(docId)
    }

    fun insertComment(comment: Comment): Completable {
        return commentDao.insert(comment)
    }

    fun deleteCommentById(id: Int): Completable {
        return commentDao.deleteById(id)
    }

    //locker
    //Suspend function 'getAll' should be called only from a coroutine or another suspend function
//    suspend fun getAllLikeData(): List<Saying> = withContext(ioDispatcher){
//        lockerDao.getAll()
//    }
    //room 이라서 알아서 io dispatcher 로 스케줄링 되는 듯.
    //수동 처리라면 ioDispatcher로 지정해줘야 함.
    suspend fun getAllLikeData(): List<Saying> {
        d("localDataSource", Thread.currentThread().name)
        return lockerDao.getAll()
    }


    fun findByDocId(docId: String): Maybe<Saying>{
        return lockerDao.findByDocId(docId)
    }

    fun insertLikeSaying(saying: Saying) : Completable{
        return lockerDao.insert(saying)
    }

    fun deleteLikeSayingByDocId(docId: String) : Completable{
        return lockerDao.delete(docId)
    }

}