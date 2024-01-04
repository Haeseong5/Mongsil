package com.cashproject.mongsil.ui.main

import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.data.firebase.fcm.PushManager
import com.cashproject.mongsil.repository.PosterRepository
import com.cashproject.mongsil.repository.model.Poster
import com.cashproject.mongsil.util.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

/**
 * TODO Coroutines + Flow 적용
 * https://medium.com/firebase-developers/firebase-ing-with-kotlin-coroutines-flow-dab1bc364816
 */

class MainViewModel(
    private val pushManager: PushManager = PushManager(),
    private val posterRepository: PosterRepository = PosterRepository()
) : BaseViewModel() {
    val allPosters: MutableList<Poster> = mutableListOf()

    private val _selectedPagePosition = MutableStateFlow<Int>(1)
    val selectedPagePosition = _selectedPagePosition.asStateFlow()

    init {
        initPushNotificationSettings()
    }

    fun selectPage(position: Int) {
        _selectedPagePosition.value = position
    }

    suspend fun loadAllPosters() {
        if (allPosters.isEmpty()) {
            allPosters.addAll(posterRepository.getAllPosters())
        }
    }

    fun getRandomSaying(date: Date): Poster {
        return posterRepository.getRandomSaying(
            date = date,
            posters = allPosters
        )
    }

    private fun initPushNotificationSettings() {
        pushManager.emitPushEvent(PreferencesManager.isEnabledPushNotification)
    }
}