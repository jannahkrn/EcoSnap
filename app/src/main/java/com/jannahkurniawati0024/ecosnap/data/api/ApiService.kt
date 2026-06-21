package com.jannahkurniawati0024.ecosnap.data.api

import com.jannahkurniawati0024.ecosnap.data.model.DeleteResponse
import com.jannahkurniawati0024.ecosnap.data.model.PostListResponse
import com.jannahkurniawati0024.ecosnap.data.model.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("posts/feed")
    suspend fun getFeed(): Response<PostListResponse>

    @GET("posts")
    suspend fun getPosts(
        @Query("userId") userId: String
    ): Response<PostListResponse>

    @Multipart
    @POST("posts")
    suspend fun createPost(
        @Part("userId") userId: RequestBody,
        @Part("userEmail") userEmail: RequestBody,
        @Part("userName") userName: RequestBody,
        @Part("userPhotoUrl") userPhotoUrl: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<PostResponse>

    @FormUrlEncoded
    @PUT("posts/{id}")
    suspend fun updatePost(
        @Path("id") postId: String,
        @Field("description") description: String,
        @Field("userId") userId: String
    ): Response<PostResponse>

    @DELETE("posts/{id}")
    suspend fun deletePost(
        @Path("id") postId: String,
        @Query("userId") userId: String
    ): Response<DeleteResponse>
}