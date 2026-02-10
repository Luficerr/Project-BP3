package com.pab.funsplash.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pab.funsplash.data.repository.PhotoRepository
import com.pab.funsplash.data.repository.BookmarkRepository

class DetailPhotoViewModelFactory(
    private val photoRepository: PhotoRepository,
    private val bookmarkRepository: BookmarkRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailPhotoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailPhotoViewModel(
                photoRepository,
                bookmarkRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}