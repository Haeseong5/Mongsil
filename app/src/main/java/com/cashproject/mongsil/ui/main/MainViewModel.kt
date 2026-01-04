package com.cashproject.mongsil.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.BuildConfig
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.data.firebase.fcm.PushManager
import com.cashproject.mongsil.repository.repository.PosterRepository
import com.cashproject.mongsil.ui.pages.calendar.CalendarScreenType
import com.cashproject.mongsil.ui.pages.calendar.defaultCalendarScreenType
import com.cashproject.mongsil.ui.pages.diary.model.Poster
import com.cashproject.mongsil.ui.pages.diary.model.toDomain
import com.cashproject.mongsil.ui.pages.diary.model.toPoster
import com.cashproject.mongsil.util.PreferencesManager
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _visibleCalendarScreenType: MutableStateFlow<CalendarScreenType> =
        MutableStateFlow(defaultCalendarScreenType)
    val visibleCalendarScreenType: StateFlow<CalendarScreenType> = _visibleCalendarScreenType

    val showPagerTutorialAnim: MutableSharedFlow<Unit> = MutableSharedFlow()

    // AdLoader 관련 상태
    private var adLoader: AdLoader? = null
    private val _nativeAd: MutableStateFlow<NativeAd?> = MutableStateFlow(null)
    val nativeAd: StateFlow<NativeAd?> = _nativeAd.asStateFlow()

    private val _isAdLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAdLoading: StateFlow<Boolean> = _isAdLoading.asStateFlow()

    // 백 버튼 다이얼로그 표시 이벤트
    val showExitDialog: MutableSharedFlow<Unit> = MutableSharedFlow()

    init {
        initPushNotificationSettings()
        loadAllPosters()
    }

    fun initAdLoader(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            // DEBUG 모드에서는 Google 제공 테스트 ID 사용 (항상 광고 표시됨)
            // RELEASE 모드에서는 실제 광고 ID 사용
            val adUnitId = if (BuildConfig.DEBUG) {
                "ca-app-pub-1939032811151400/9104485125"
//                "ca-app-pub-3940256099942544/2247696110" // Google 제공 테스트 네이티브 광고 ID (Advanced)
            } else {
                context.getString(R.string.ad_native_id) // ca-app-pub-1939032811151400/9104485125
            }

            Log.d(
                "++##",
                "initAdLoader - BuildConfig.DEBUG: ${BuildConfig.DEBUG}, adUnitId: $adUnitId"
            )

            adLoader = AdLoader.Builder(context, adUnitId)
                .forNativeAd { nativeAd ->
                    viewModelScope.launch {
                        Log.d("++##", "✅ Native Ad loaded successfully!")
                        Log.d("++##", "  - Headline: ${nativeAd.headline}")
                        Log.d("++##", "  - Body: ${nativeAd.body}")
                        Log.d("++##", "  - Call to Action: ${nativeAd.callToAction}")
                        _nativeAd.value = nativeAd
                        _isAdLoading.value = false
                    }
                }
                .withAdListener(object : com.google.android.gms.ads.AdListener() {
                    override fun onAdFailedToLoad(adError: com.google.android.gms.ads.LoadAdError) {
                        super.onAdFailedToLoad(adError)
                        viewModelScope.launch {
                            Log.e("++##", "❌ Native Ad failed to load")
                            Log.e("++##", "  - Error Code: ${adError.code}")
                            Log.e("++##", "  - Error Message: ${adError.message}")
                            Log.e("++##", "  - Error Domain: ${adError.domain}")
                            Log.e("++##", "  - Error Cause: ${adError.cause}")

                            // 주요 에러 코드 해석
                            when (adError.code) {
                                0 -> Log.e("++##", "  → ERROR_CODE_INTERNAL_ERROR: 내부 오류")
                                1 -> Log.e(
                                    "++##",
                                    "  → ERROR_CODE_INVALID_REQUEST: 잘못된 요청 (광고 단위 ID 확인)"
                                )

                                2 -> Log.e("++##", "  → ERROR_CODE_NETWORK_ERROR: 네트워크 오류")
                                3 -> Log.e(
                                    "++##",
                                    "  → ERROR_CODE_NO_FILL: 표시할 광고 없음 (정상적인 현상일 수 있음)"
                                )
                            }

                            _isAdLoading.value = false
                        }
                    }

                    override fun onAdOpened() {
                        super.onAdOpened()
                        Log.d("++##", "Native Ad opened")
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        Log.d("++##", "Native Ad clicked")
                    }

                    override fun onAdClosed() {
                        super.onAdClosed()
                        Log.d("++##", "Native Ad closed")
                    }
                })
                .build()
        }
    }

    /**
     * 백 버튼 처리 - MainFragment에서 호출
     * @return true면 백 버튼 이벤트 소비, false면 기본 동작 수행
     */
    fun handleBackPressed(): Boolean {
        viewModelScope.launch {
            showExitDialog.emit(Unit)
        }
        return true // 백 버튼 이벤트 소비
    }

    /**
     * 종료 다이얼로그가 표시될 때 광고 로드
     */
    fun loadAdForExitDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("++##", "loadAdForExitDialog() called")
            _isAdLoading.value = true

            if (adLoader == null) {
                Log.e("++##", "❌ AdLoader is null! Cannot load ad.")
                _isAdLoading.value = false
                return@launch
            }

            adLoader?.let { loader ->
                val adRequest = AdRequest.Builder().build()
                Log.d("++##", "📡 Requesting native ad...")
                loader.loadAd(adRequest)
            }
        }
    }

    fun cleanupAd() {
        _nativeAd.value?.destroy()
        _nativeAd.value = null
        _isAdLoading.value = false
    }

    fun emitPagerTutorialAnimEvent() {
        viewModelScope.launch {
            showPagerTutorialAnim.emit(Unit)
        }
    }

    private fun loadAllPosters() {
        viewModelScope.launch {
            try {
                _allPosters.emit(posterRepository.getAllPosters().toPoster())
            } catch (e: Exception) {
                error.emit(e)
            }
        }
    }

    fun getRandomSaying(date: Date): Poster {
        return posterRepository.getRandomSaying(
            date = date,
            posters = allPosters.value.toDomain()
        ).toPoster()
    }

    private fun initPushNotificationSettings() {
        pushManager.emitPushEvent(PreferencesManager.isEnabledPushNotification)
    }

    fun toggleCalendarScreenType(calendarScreenType: CalendarScreenType) {
        viewModelScope.launch {
            when (calendarScreenType) {
                CalendarScreenType.DEFAULT -> {
                    _visibleCalendarScreenType.emit(CalendarScreenType.LIST)
                }

                CalendarScreenType.LIST -> {
                    _visibleCalendarScreenType.emit(CalendarScreenType.DEFAULT)
                }
            }
        }
    }
}