package com.pab.funsplash.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pab.funsplash.data.repository.BookmarkRepository

class BookmarkViewModelFactory(
    private val repository: BookmarkRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookmarkViewModel(repository) as T
    }
}
