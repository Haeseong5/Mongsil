package com.cashproject.mongsil.kmp.screen.setting.screenlock

import androidx.fragment.app.FragmentActivity
import java.lang.ref.WeakReference

class CurrentActivityHolder {
    private var currentActivityRef: WeakReference<FragmentActivity>? = null

    fun set(activity: FragmentActivity) {
        currentActivityRef = WeakReference(activity)
    }

    fun clear(activity: FragmentActivity) {
        val current = currentActivityRef?.get()
        if (current === activity) {
            currentActivityRef?.clear()
            currentActivityRef = null
        }
    }

    fun get(): FragmentActivity? = currentActivityRef?.get()
}
