package com.pab.funsplash.data.api

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path
import com.pab.funsplash.data.model.UnsplashPhoto
import com.pab.funsplash.data.model.PhotoDetail

interface UnsplashApi {

    @GET("photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10,
        @Query("client_id") clientId: String
    ): List<UnsplashPhoto>

    @GET("photos/{id}")
    suspend fun getPhotoDetail(
        @Path("id") id: String,
        @Query("client_id") clientId: String
    ): PhotoDetail
}