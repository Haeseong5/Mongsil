package com.cashproject.mongsil.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.cashproject.mongsil.extension.addTo
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class ClickUtil(lifecycle: Lifecycle, private val delay: Long = 500L) : LifecycleObserver {
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var clickSubject: PublishSubject<(() -> Unit)>

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun create() {
        compositeDisposable = CompositeDisposable()
        clickSubject = PublishSubject.create<(() -> Unit)>()
        clickSubject.throttleFirst(delay, TimeUnit.MILLISECONDS)
            .subscribe(
                { it.invoke() },
                { it.localizedMessage } //When subscribe, Consumer is that handle Exception and Prevent to crash ppp
            ).addTo(compositeDisposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        compositeDisposable.dispose()
    }

    fun run(hof: () -> Unit) {
        clickSubject.onNext(hof)
    }
}