package com.cashproject.mongsil.kmp.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics
import dev.gitlive.firebase.analytics.logEvent
import dev.gitlive.firebase.crashlytics.crashlytics

class FirebaseServiceImpl : FirebaseService {

    private val analytics = Firebase.analytics
    private val crashlytics = Firebase.crashlytics

    // 화면 진입, 버튼 클릭 등 사용자 행동 이벤트 기록
    override fun logEvent(name: String, params: Map<String, String>) {
        analytics.logEvent(name) {
            params.forEach { (key, value) -> param(key, value) }
        }
    }

    // 앱이 죽지 않는 예외(네트워크 오류, 파싱 실패 등) 추적용.
    // try-catch로 처리했지만 Firebase에는 기록하고 싶을 때 사용.
    // (앱 크래시는 Crashlytics가 자동으로 기록함)
    override fun recordException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    // 크래시 직전 흐름 파악을 위한 breadcrumb 로그.
    // 로그 자체는 전송되지 않고, 이후 크래시 발생 시 리포트에 함께 첨부됨.
    override fun log(message: String) {
        crashlytics.log(message)
    }

    override fun setUserId(userId: String?) {
        analytics.setUserId(userId)
        userId?.let { crashlytics.setUserId(it) }
    }
}
