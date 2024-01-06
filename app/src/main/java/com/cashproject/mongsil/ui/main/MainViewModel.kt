package com.cashproject.mongsil.ui.main

import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.data.firebase.fcm.PushManager
import com.cashproject.mongsil.repository.PosterRepository
import com.cashproject.mongsil.repository.model.Poster
import com.cashproject.mongsil.util.PreferencesManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

 class MainViewModel(
     private val pushManager: PushManager = PushManager(),
     private val posterRepository: PosterRepository = PosterRepository()
 ) : BaseViewModel() {

     private val _allPosters: MutableStateFlow<List<Poster>> = MutableStateFlow(emptyList())
     val allPosters = _allPosters.asStateFlow()

     val error = MutableSharedFlow<Throwable>()

     val currentPage = MutableStateFlow(1)

     init {
         initPushNotificationSettings()
         loadAllPosters()
     }

     private fun loadAllPosters() {
         viewModelScope.launch {
             try {
                 _allPosters.emit(posterRepository.getAllPosters())
             } catch (e: Exception) {
                 error.emit(e)
             }
         }
     }

     fun getRandomSaying(date: Date): Poster {
         return posterRepository.getRandomSaying(
             date = date,
             posters = allPosters.value
         )
     }

     private fun initPushNotificationSettings() {
         pushManager.emitPushEvent(PreferencesManager.isEnabledPushNotification)
     }
 }