package com.pab.funsplash.data.repository

import android.util.Log
import com.pab.funsplash.BuildConfig
import com.pab.funsplash.data.model.UnsplashPhoto
import com.pab.funsplash.data.model.PhotoDetail
import com.pab.funsplash.data.api.UnsplashApi

class PhotoRepository(
    private val api: UnsplashApi
) {

    private var currentPage = 1
    private val cache = mutableListOf<UnsplashPhoto>()

    suspend fun getPhotos(forceRefresh: Boolean = false): List<UnsplashPhoto> {
        if (forceRefresh) {
            currentPage = 1
            cache.clear()
        }

        val response = api.getPhotos(
            page = currentPage,
            clientId = BuildConfig.UNSPLASH_ACCESS_KEY
        )

        if (response.isNotEmpty()) {
            currentPage++
            cache.addAll(response)
        }

        return cache
    }

    suspend fun getPhotoDetail(photoId: String): PhotoDetail {
        return api.getPhotoDetail(
            id = photoId,
            clientId = BuildConfig.UNSPLASH_ACCESS_KEY
        )
    }
}