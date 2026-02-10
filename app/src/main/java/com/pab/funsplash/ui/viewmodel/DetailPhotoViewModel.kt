package com.pab.funsplash.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pab.funsplash.data.model.PhotoDetail
import com.pab.funsplash.data.repository.BookmarkRepository
import com.pab.funsplash.data.repository.PhotoRepository
import kotlinx.coroutines.launch

class DetailPhotoViewModel(
    private val repository: PhotoRepository,
    private val bookmarkRepo: BookmarkRepository
) : ViewModel() {

    private val _photo = MutableLiveData<PhotoDetail>()
    val photo: LiveData<PhotoDetail> = _photo

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _isBookmarked = MutableLiveData<Boolean>()
    val isBookmarked: LiveData<Boolean> = _isBookmarked
    private val _bookmarkEvent = MutableLiveData<Boolean>()
    val bookmarkEvent: LiveData<Boolean> = _bookmarkEvent

    fun loadPhoto(photoId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _photo.value = repository.getPhotoDetail(photoId)
            } catch (e: Exception) {
                Log.e("DETAIL", "Load detail failed", e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun checkBookmark(photoId: String) {
        viewModelScope.launch {
            _isBookmarked.value = bookmarkRepo.isBookmarked(photoId)
        }
    }

    fun toggleBookmark(photo: PhotoDetail) {
        viewModelScope.launch {
            val result = bookmarkRepo.toggleBookmark(photo)
            _isBookmarked.value = result
            _bookmarkEvent.value = result
        }
    }
}