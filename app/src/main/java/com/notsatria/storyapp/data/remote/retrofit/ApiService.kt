package com.notsatria.storyapp.data.remote.retrofit

import com.notsatria.storyapp.data.remote.response.DetailStoryResponse
import com.notsatria.storyapp.data.remote.response.FetchStoriesResponse
import com.notsatria.storyapp.data.remote.response.LoginResponse
import com.notsatria.storyapp.data.remote.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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
        @Header("Authorization") token: String
    ): FetchStoriesResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailStoryResponse
}