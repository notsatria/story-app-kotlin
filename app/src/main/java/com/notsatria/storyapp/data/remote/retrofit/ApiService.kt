package com.notsatria.storyapp.data.remote.retrofit

import com.notsatria.storyapp.data.remote.response.DetailStoryResponse
import com.notsatria.storyapp.data.remote.response.ErrorResponse
import com.notsatria.storyapp.data.remote.response.FetchStoriesResponse
import com.notsatria.storyapp.data.remote.response.LoginResponse
import com.notsatria.storyapp.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun fetchAllStories(
    ): FetchStoriesResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String
    ): DetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ): ErrorResponse
}