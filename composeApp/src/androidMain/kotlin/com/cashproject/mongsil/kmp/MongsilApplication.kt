package com.cashproject.mongsil.kmp

import android.app.Application
import com.cashproject.mongsil.kmp.di.platformModule
import com.cashproject.mongsil.kmp.mongsilAppDeclaration
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue

class MongsilApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(
            mongsilAppDeclaration {
                androidContext(this@MongsilApplication)
                modules(platformModule())
            }
        )
    }

    override fun onTerminate() {
        super.onTerminate()

        // HttpClient 리소스 정리
        try {
            val httpClient: HttpClient by inject(HttpClient::class.java)
            httpClient.close()
        } catch (e: Exception) {
            // 이미 정리되었거나 초기화되지 않은 경우 무시
            e.printStackTrace()
        }
        stopKoin()
    }
}
