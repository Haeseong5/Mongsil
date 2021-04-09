package com.cashproject.mongsil.util

import android.util.Log
import io.reactivex.subjects.PublishSubject

object RxEventBus {
    private val calendarEventBus = PublishSubject.create<Boolean>()
    private val likeEventBus = PublishSubject.create<Boolean>()
    private val resumeEventBus = PublishSubject.create<Boolean>()
    private val homeEventBus = PublishSubject.create<Boolean>()

    //Calendar
    fun sendToCalendar(isUpdated: Boolean){
        Log.d("RxEventBus", "sendToCalendar() $isUpdated")
        calendarEventBus.onNext(isUpdated)
    }

    fun toCommentObservable(): PublishSubject<Boolean>{
        return calendarEventBus
    }

    //Locker
    fun sendToLocker(isUpdated: Boolean){
        Log.d("RxEventBus", "sendToLocker() $isUpdated")
        likeEventBus.onNext(isUpdated)
    }

    fun toLikeObservable(): PublishSubject<Boolean>{
        return likeEventBus
    }

    fun sendToFragment(isResumed: Boolean){
        Log.d("RxEventBus", "sendToFragment() $isResumed")
        resumeEventBus.onNext(isResumed)
    }

    fun toResumedObservable(): PublishSubject<Boolean>{
        return resumeEventBus
    }

    fun sendToHome(isResumed: Boolean){
        Log.d("RxEventBus", "sendToFragment() $isResumed")
        homeEventBus.onNext(isResumed)

    }

    fun toHomeObservable(): PublishSubject<Boolean>{
        return homeEventBus
    }
}
