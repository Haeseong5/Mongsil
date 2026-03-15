package com.cashproject.mongsil.kmp.firebase

interface FirebaseService {
    fun logEvent(name: String, params: Map<String, String> = emptyMap())
    fun recordException(throwable: Throwable)
    fun log(message: String)
    fun setUserId(userId: String?)
}
