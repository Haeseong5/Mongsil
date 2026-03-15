package com.cashproject.mongsil.kmp.firebase

// TODO: iOS Firebase 활성화 방법
// 1. composeApp/build.gradle.kts의 iosMain.dependencies에 아래 추가:
//    implementation(libs.gitlive.firebase.analytics)
//    implementation(libs.gitlive.firebase.crashlytics)
// 2. Xcode에서 Firebase iOS SDK를 SPM으로 추가:
//    - FirebaseAnalytics, FirebaseCrashlytics 패키지 선택
// 3. 이 파일을 아래 Android 구현체와 동일하게 수정
class FirebaseServiceImpl : FirebaseService {
    override fun logEvent(name: String, params: Map<String, String>) = Unit
    override fun recordException(throwable: Throwable) = Unit
    override fun log(message: String) = Unit
    override fun setUserId(userId: String?) = Unit
}
