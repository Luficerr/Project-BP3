package com.pab.funsplash.data.repository

import com.pab.funsplash.data.local.BookmarkDao
import com.pab.funsplash.data.model.PhotoDetail
import com.pab.funsplash.data.model.BookmarkEntity

class BookmarkRepository(private val dao: BookmarkDao) {

    fun getBookmarks() = dao.getAllBookmarks()

    suspend fun isBookmarked(id: String) =
        dao.isBookmarked(id)

    suspend fun insert(bookmark: BookmarkEntity) {
        dao.insert(bookmark)
    }

    suspend fun delete(bookmark: BookmarkEntity) {
        dao.deleteById(bookmark.photoId)
    }

    suspend fun toggleBookmark(photo: PhotoDetail): Boolean {
        return if (dao.isBookmarked(photo.id)) {
            dao.deleteById(photo.id)
            false
        } else {
            dao.insert(
                BookmarkEntity(
                    photoId = photo.id,
                    imageUrl = photo.urls.regular
                        ?: photo.urls.small
                        ?: photo.urls.full
                        ?: "",
                    author = photo.user.name,
                    likes = photo.likes,
                    description = photo.description
                )
            )
            true
        }
    }
}
