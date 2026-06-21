package com.jannahkurniawati0024.ecosnap.data.repository

import com.jannahkurniawati0024.ecosnap.data.api.RetrofitInstance
import com.jannahkurniawati0024.ecosnap.data.model.Post
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class PostRepository {

    private val api = RetrofitInstance.api

    suspend fun getFeed(): Result<List<Post>> {
        return try {
            val response = api.getFeed()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Gagal memuat feed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserPosts(userId: String): Result<List<Post>> {
        return try {
            val response = api.getPosts(userId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Gagal memuat postingan"))
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
        imageFile: File
    ): Result<Post> {
        return try {
            val userIdBody = userId.toRequestBody("text/plain".toMediaTypeOrNull())
            val userEmailBody = userEmail.toRequestBody("text/plain".toMediaTypeOrNull())
            val userNameBody = userName.toRequestBody("text/plain".toMediaTypeOrNull())
            val userPhotoUrlBody = userPhotoUrl.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val imageRequestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)

            val response = api.createPost(
                userIdBody, userEmailBody, userNameBody,
                userPhotoUrlBody, descriptionBody, imagePart
            )
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Gagal membuat postingan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePost(
        postId: String,
        description: String,
        userId: String
    ): Result<Post> {
        return try {
            val response = api.updatePost(postId, description, userId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Gagal mengedit postingan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePost(postId: String, userId: String): Result<Boolean> {
        return try {
            val response = api.deletePost(postId, userId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(true)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Gagal menghapus postingan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}