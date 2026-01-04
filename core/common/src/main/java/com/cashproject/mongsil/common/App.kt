package com.cashproject.mongsil.common

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration


class App : Application() {

    companion object {
        //TODO 이관
        lateinit var prefs: SharedPreferences
//        var appVersion: AppVersion = AppVersion()

        @SuppressLint("StaticFieldLeak")
        lateinit var instance: App
            private set

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }


    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
        instance = this

        //TODO 이관
        prefs = getSharedPreferences("prefs", 0)

        // MobileAds 초기화
        MobileAds.initialize(this) { initializationStatus ->
            Log.d("++##", "MobileAds initialized")
            initializationStatus.adapterStatusMap.forEach { (adapterName, status) ->
                Log.d(
                    "++##",
                    "Adapter: $adapterName, Status: ${status.initializationState}, Latency: ${status.latency}"
                )
            }
        }

        // 테스트 기기 설정 (개발/테스트 중에만 사용, 릴리즈 전에 제거할 것)
        // 테스트하려면 아래 주석을 해제하고 실제 기기 ID를 입력하세요
        // Logcat에서 "Use RequestConfiguration.Builder().setTestDeviceIds" 메시지를 확인
        //EFD451145EB7B6E8AE7A2D8FF9140452
         val testDeviceIds = listOf("YOUR_TEST_DEVICE_ID_HERE")
         val configuration = RequestConfiguration.Builder()
             .setTestDeviceIds(testDeviceIds)
             .build()
         MobileAds.setRequestConfiguration(configuration)
         Log.d("++##", "Test devices configured: $testDeviceIds")
    }
}