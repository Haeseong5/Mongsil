package com.cashproject.mongsil.kmp.di

import com.cashproject.mongsil.kmp.network.HttpClientFactory
import com.cashproject.mongsil.kmp.network.api.EmoticonApi
import com.cashproject.mongsil.kmp.repository.EmoticonRepository
import org.koin.dsl.module

/**
 * 네트워크 모듈
 * API 클라이언트 및 Repository 등록
 */
internal val networkModule = module {
    // HttpClient (싱글톤)
    single { HttpClientFactory.create() }
    
    // API 클라이언트
    single { EmoticonApi(get()) }
    
    // Repository (DB 캐싱 지원)
    single { EmoticonRepository(emoticonApi = get(), database = get()) }
}
