package com.cashproject.mongsil.ui.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.data.api.PosterApi
import com.cashproject.mongsil.data.firebase.fcm.PushManager
import com.cashproject.mongsil.data.service.PosterService
import com.cashproject.mongsil.repository.mapper.toPosters
import com.cashproject.mongsil.repository.model.Poster
import com.cashproject.mongsil.util.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.random.Random

/**
 * TODO Coroutines + Flow 적용
 * https://medium.com/firebase-developers/firebase-ing-with-kotlin-coroutines-flow-dab1bc364816
 */

class MainViewModel(
    private val posterApi: PosterApi = PosterService,
    private val pushManager: PushManager = PushManager(),
) : BaseViewModel() {

    // 캘린더 리스트
    private val _allPosters: MutableStateFlow<List<Poster>> = MutableStateFlow(emptyList())
    val allPosters: StateFlow<List<Poster>> get() = _allPosters.asStateFlow()

    private val _selectedPagePosition = MutableStateFlow<Int>(1)
    val selectedPagePosition = _selectedPagePosition.asStateFlow()

    private val hashMap = HashMap<Long, Int>()

    init {
        initPushNotificationSettings()
        getSayingList()
    }

    fun selectPage(position: Int) {
        _selectedPagePosition.value = position
    }

    private fun getSayingList() {
        viewModelScope.launch {
            try {
                _allPosters.emit(posterApi.getAllPosters().toPosters())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getRandomSaying(date: Date): Poster {
        return try {
            val day = date.time
            val sayings = allPosters.value
            val cachedIdx = hashMap[day]
            if (cachedIdx == null) {
                val randomIdx = Random.nextInt(sayings.size)
                hashMap[day] = randomIdx
                sayings[randomIdx]
            } else {
                sayings[cachedIdx]
            }
        } catch (e: Exception) {
            Log.e(this.javaClass.name, e.localizedMessage)
            errorSubject.onNext(e)
            Poster("", "", "")
        }
    }

    private fun initPushNotificationSettings() {
        pushManager.emitPushEvent(PreferencesManager.isEnabledPushNotification)
    }
}