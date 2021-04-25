package com.cashproject.mongsil.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.model.db.datasource.LocalDataSource
import kotlinx.coroutines.*

/**
 * Dispatchers.Main -
 * 이 디스패처를 사용하여 기본 Android 스레드에서 코루틴을 실행합니다.
 * 이 디스패처는 UI와 상호작용하고 빠른 작업을 실행하기 위해서만 사용해야 합니다.
 * 예를 들어 suspend 함수를 호출하고 Android UI 프레임워크 작업을 실행하며 LiveData 객체를 업데이트합니다.
 *
 * Dispatchers.Default -
 * 이 디스패처는 CPU를 많이 사용하는 작업을 기본 스레드 외부에서 실행하도록 최적화되어 있습니다.
 * 예를 들어 목록을 정렬하고 JSON을 파싱합니다.
 */
class LockerViewModel(
    private val localDataSource: LocalDataSource
) : BaseViewModel() {

    val job = Job()
    /* 새 코루틴을 만들거나 withContext() 를 호출할 때 Dispatchers 를 하드코딩하지 마세요. */
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val _likeData = MutableLiveData<List<Saying>>()
    val likeData: LiveData<List<Saying>>
        get() = _likeData


    /**
     * This scope will be canceled when ViewModel will be cleared, i.e [ViewModel.onCleared] is called
     * viewModelScope 는 ViewModel 이 clear 될 때 취소 될 것입니다. (ViewModel.onCleared()가 불릴 때 취소된다.)
     *  viewModelScope 속성은 Dispatchers.Main 으로 하드코딩됩니다.
     *
     *  ViewModel 클래스를 사용하면 비즈니스 로직을 실행하기 위해 정지 함수를 노출하는 대신 코루틴을 만들게 됩니다.
     *  데이터 스트림을 사용하여 상태를 노출하는 대신 하나의 값만 방출해야 하는 경우 ViewModel의 정지 함수가 유용합니다.
     */
    fun getAllLike() {
        Log.d(TAG, "코루틴 밖: "+ Thread.currentThread().name)
        viewModelScope.launch(ioDispatcher + job) {
            Log.d("viewModelScope", Thread.currentThread().name)
            val result = localDataSource.getAllLikeData()
            _likeData.postValue(result)
        }
    }
}