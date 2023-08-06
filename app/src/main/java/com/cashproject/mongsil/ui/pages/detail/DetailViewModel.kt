package com.cashproject.mongsil.ui.pages.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.data.db.entity.SayingEntity
import com.cashproject.mongsil.data.service.DiaryService
import com.cashproject.mongsil.data.firebase.FireStoreDataSource
import com.cashproject.mongsil.data.service.BookmarkService
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class DetailViewModel(
    private val bookmarkService: BookmarkService = BookmarkService(),
) : BaseViewModel() {

    private val _isLike = MutableLiveData<Boolean>()
    val isLike: LiveData<Boolean>
        get() = _isLike

    val db by lazy {
        Firebase.firestore
    }

    fun like(sayingEntity: SayingEntity) {
        viewModelScope.launch {
            try {
                bookmarkService.insertBookmarkPoster(sayingEntity.docId)
            } catch (e: Exception) {
                errorSubject.onNext(e)
            }
        }
    }

    fun unLike(docId: String) {
        viewModelScope.launch {
            try {
                bookmarkService.deleteBookmarkPoster(docId)
                _isLike.postValue(false)
            } catch (e: Exception) {
                errorSubject.onNext(e)
            }
        }
    }


    fun findByDocId(docId: String) {
        try {
            viewModelScope.launch {
                val poster = bookmarkService.findPosterById(docId)
                _isLike.value = true //TODO ?
            }
        } catch (e: Exception) {
            errorSubject.onNext(e)
        }
    }
}