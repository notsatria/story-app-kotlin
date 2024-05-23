package com.notsatria.storyapp.data.remote.retrofit

import com.notsatria.storyapp.data.remote.response.DetailStoryResponse
import com.notsatria.storyapp.data.remote.response.ErrorResponse
import com.notsatria.storyapp.data.remote.response.StoryResponse
import com.notsatria.storyapp.data.remote.response.LoginResponse
import com.notsatria.storyapp.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Auth
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
    // Auth

    // Stories
    @GET("stories")
    suspend fun fetchAllStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20,
    ): StoryResponse

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

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location : Int = 1,
    ): StoryResponse
    // Stories
}