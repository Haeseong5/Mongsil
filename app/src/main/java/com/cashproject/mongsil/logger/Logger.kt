package com.cashproject.mongsil.logger

import android.os.Bundle
import com.cashproject.mongsil.extension.toBundle
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics


object Logger {

    private val firebaseAnalytics: FirebaseAnalytics by lazy { Firebase.analytics }

    fun sendLog(
        key: String,
        value: Map<String, Any>?
    ) {
        sendFirebaseLog(key, value?.toBundle())
    }

    private fun sendFirebaseLog(
        key: String,
        bundle: Bundle?
    ) {
        firebaseAnalytics.logEvent(
            key,
            bundle
        )
    }
}