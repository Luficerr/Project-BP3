package com.pab.funsplash.data.model

import com.google.gson.annotations.SerializedName

data class PhotoDetail(
    val id: String,

    @SerializedName("alt_description")
    val altDescription: String?,

    val description: String?,

    val likes: Int,

    val downloads: Int?,

    val urls: Urls,

    val user: User
)

data class ProfileImage(
    val small: String?,
    val medium: String?,
    val large: String?
)
