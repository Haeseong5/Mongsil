package com.cashproject.mongsil.util

import android.util.Log
import io.reactivex.subjects.PublishSubject

object RxEventBus {
    private val calendarEventBus = PublishSubject.create<Boolean>()
    private val likeEventBus = PublishSubject.create<Boolean>()

    fun sendToCalendar(isUpdated: Boolean){
        Log.d("RxEventBus", "sendToCalendar() $isUpdated")
        calendarEventBus.onNext(isUpdated)
    }

    fun toCommentObservable(): PublishSubject<Boolean>{
        return calendarEventBus
    }

    fun sendToLocker(isUpdated: Boolean){
        Log.d("RxEventBus", "sendToLocker() $isUpdated")
        likeEventBus.onNext(isUpdated)
    }

    fun toLikeObservable(): PublishSubject<Boolean>{
        return likeEventBus
    }
}
