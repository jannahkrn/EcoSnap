package com.jannahkurniawati0024.ecosnap.data.model

import com.squareup.moshi.Json

data class Post(
    @Json(name = "id") val id: String = "",
    @Json(name = "userId") val userId: String = "",
    @Json(name = "userEmail") val userEmail: String = "",
    @Json(name = "userName") val userName: String = "",
    @Json(name = "userPhotoUrl") val userPhotoUrl: String = "",
    @Json(name = "description") val description: String = "",
    @Json(name = "imageUrl") val imageUrl: String = "",
    @Json(name = "createdAt") val createdAt: String = ""
)

data class PostResponse(
    val success: Boolean = true,
    val message: String = "",
    val data: Post? = null
)

data class PostListResponse(
    val success: Boolean = true,
    val message: String = "",
    val data: List<Post> = emptyList()
)

data class DeleteResponse(
    val success: Boolean = true,
    val message: String = ""
)