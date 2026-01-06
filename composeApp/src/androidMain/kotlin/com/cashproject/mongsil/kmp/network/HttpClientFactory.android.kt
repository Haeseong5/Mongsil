package com.cashproject.mongsil.kmp.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

/**
 * Android용 Ktor 엔진 (OkHttp)
 */
actual fun getPlatformEngine(): HttpClientEngine {
    return OkHttp.create()
}
