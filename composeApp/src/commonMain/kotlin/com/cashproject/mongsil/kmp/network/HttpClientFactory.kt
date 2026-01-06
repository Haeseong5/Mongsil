package com.cashproject.mongsil.kmp.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Ktor HttpClient를 생성하는 Factory
 * 플랫폼별 엔진을 사용합니다.
 */
object HttpClientFactory {
    
    /**
     * 공통 HttpClient 생성
     * JSON 직렬화, 로깅 등의 플러그인이 설정됩니다.
     */
    fun create(): HttpClient {
        return HttpClient(getPlatformEngine()) {
            // JSON 직렬화 설정
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            
            // 로깅 설정
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("HTTP Client: $message")
                    }
                }
                level = LogLevel.INFO
            }
        }
    }
}

/**
 * 플랫폼별 엔진을 반환합니다.
 * Android: OkHttp
 * iOS: Darwin
 */
expect fun getPlatformEngine(): io.ktor.client.engine.HttpClientEngine
