package com.pab.funsplash.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.pab.funsplash.data.repository.BookmarkRepository
import com.pab.funsplash.data.model.BookmarkEntity

class BookmarkViewModel(
    private val repository: BookmarkRepository
) : ViewModel() {

    val bookmarks = repository.getBookmarks().asLiveData()

    fun deleteBookmark(bookmark: BookmarkEntity) {
        viewModelScope.launch {
            repository.delete(bookmark)
        }
    }

    fun restoreBookmark(bookmark: BookmarkEntity) {
        viewModelScope.launch {
            repository.insert(bookmark)
        }
    }
}
