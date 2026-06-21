package com.jannahkurniawati0024.ecosnap.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
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

@JsonClass(generateAdapter = true)
data class PostResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "message") val message: String = "",
    @Json(name = "data") val data: Post? = null
)

@JsonClass(generateAdapter = true)
data class PostListResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "message") val message: String = "",
    @Json(name = "data") val data: List<Post> = emptyList()
)

@JsonClass(generateAdapter = true)
data class DeleteResponse(
    @Json(name = "success") val success: Boolean = false,
    @Json(name = "message") val message: String = ""
)