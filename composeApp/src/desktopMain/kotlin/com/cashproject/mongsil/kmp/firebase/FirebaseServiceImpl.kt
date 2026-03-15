package com.cashproject.mongsil.kmp.firebase

class FirebaseServiceImpl : FirebaseService {
    override fun logEvent(name: String, params: Map<String, String>) = Unit
    override fun recordException(throwable: Throwable) = Unit
    override fun log(message: String) = Unit
    override fun setUserId(userId: String?) = Unit
}
