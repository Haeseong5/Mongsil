package com.cashproject.mongsil.data.api

import com.cashproject.mongsil.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

object ApiProvider {

    private const val LONG_REQUEST_TIME_OUT = 60 * 1000L

    private fun createLongOkHttpClientBuilder(): OkHttpClient.Builder {
        val apiClientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = (HttpLoggingInterceptor.Level.BODY)
            apiClientBuilder.addInterceptor(loggingInterceptor)
        }
        apiClientBuilder.readTimeout(LONG_REQUEST_TIME_OUT, TimeUnit.MILLISECONDS)
        apiClientBuilder.writeTimeout(LONG_REQUEST_TIME_OUT, TimeUnit.MILLISECONDS)
        apiClientBuilder.connectTimeout(LONG_REQUEST_TIME_OUT, TimeUnit.MILLISECONDS)

        return apiClientBuilder
    }

    private val contentType = "application/json".toMediaType()

    @ExperimentalSerializationApi
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    /**
     * 리플렉션: 런타임에 프로그램의 구조(객체, 함수, 프로퍼티 등)를 알아내는 기법
     * kClass<T> : 클래스를 나타내며 introspection(자기 성찰) capabilities(능력) 기능을 제공합니다.
     * ::class 구문으로 이 클래스의 인스턴스를 가져올 수 있습니다.
     */
    @ExperimentalSerializationApi
    fun <T : Any> of(kClass: KClass<T>): T = Retrofit.Builder()
        .baseUrl("https://haeseong5.github.io/")
        .client(createLongOkHttpClientBuilder().build())
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()
        .create(kClass.java)
}