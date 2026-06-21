package com.jannahkurniawati0024.ecosnap.data.api

import com.jannahkurniawati0024.ecosnap.data.model.DeleteResponse
import com.jannahkurniawati0024.ecosnap.data.model.Post
import com.jannahkurniawati0024.ecosnap.data.model.PostResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("posts")
    suspend fun getFeed(): Response<List<Post>>

    @POST("posts")
    suspend fun createPost(
        @Body post: Post
    ): Response<Post>

    @PUT("posts/{id}")
    suspend fun updatePost(
        @Path("id") postId: String,
        @Body post: Post
    ): Response<Post>

    @DELETE("posts/{id}")
    suspend fun deletePost(
        @Path("id") postId: String
    ): Response<Post>
}