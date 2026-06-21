package com.jannahkurniawati0024.ecosnap.data.repository

import com.jannahkurniawati0024.ecosnap.data.api.RetrofitInstance
import com.jannahkurniawati0024.ecosnap.data.model.Post
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostRepository {

    private val api = RetrofitInstance.api

    suspend fun getFeed(): Result<List<Post>> {
        return try {
            val response = api.getFeed()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Gagal memuat feed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createPost(
        userId: String,
        userEmail: String,
        userName: String,
        userPhotoUrl: String,
        description: String,
        imageUrl: String
    ): Result<Post> {
        return try {
            val dateStr = SimpleDateFormat(
                "dd MMM yyyy, HH:mm",
                Locale("id", "ID")
            ).format(Date())

            val post = Post(
                userId = userId,
                userEmail = userEmail,
                userName = userName,
                userPhotoUrl = userPhotoUrl,
                description = description,
                imageUrl = imageUrl,
                createdAt = dateStr
            )
            val response = api.createPost(post)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Gagal membuat postingan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePost(
        postId: String,
        description: String,
        userId: String,
        userName: String,
        userEmail: String,
        userPhotoUrl: String,
        imageUrl: String,
        createdAt: String
    ): Result<Post> {
        return try {
            val post = Post(
                id = postId,
                userId = userId,
                userName = userName,
                userEmail = userEmail,
                userPhotoUrl = userPhotoUrl,
                description = description,
                imageUrl = imageUrl,
                createdAt = createdAt
            )
            val response = api.updatePost(postId, post)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Gagal mengedit postingan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePost(postId: String, userId: String): Result<Boolean> {
        return try {
            val response = api.deletePost(postId)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Gagal menghapus postingan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}