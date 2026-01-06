package com.cashproject.mongsil.kmp.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

/**
 * iOS용 Ktor 엔진 (Darwin)
 */
actual fun getPlatformEngine(): HttpClientEngine {
    return Darwin.create()
}
