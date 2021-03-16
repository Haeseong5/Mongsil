package com.cashproject.mongsil.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class ClickUtil(lifecycle: Lifecycle, private val delay: Long = 500L) : LifecycleObserver {
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var clickSubject: BehaviorSubject<(() -> Unit)>

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun create() {
        compositeDisposable = CompositeDisposable()
        clickSubject = BehaviorSubject.create<(() -> Unit)>()
        compositeDisposable.add(
            clickSubject.throttleFirst(delay, TimeUnit.MILLISECONDS)
                .subscribe {
                    it.invoke()
                })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        compositeDisposable.dispose()
    }

    fun run(hof: () -> Unit) {
        clickSubject.onNext(hof)
    }
}