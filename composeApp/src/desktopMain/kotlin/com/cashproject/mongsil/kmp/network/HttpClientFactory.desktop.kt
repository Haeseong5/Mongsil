package com.cashproject.mongsil.kmp.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO

actual fun getPlatformEngine(): HttpClientEngine = CIO.create()
