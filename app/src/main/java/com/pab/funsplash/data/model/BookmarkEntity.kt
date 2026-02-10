package com.pab.funsplash.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val photoId: String,
    val imageUrl: String,
    val author: String,
    val likes: Int,
    val description: String?
)
