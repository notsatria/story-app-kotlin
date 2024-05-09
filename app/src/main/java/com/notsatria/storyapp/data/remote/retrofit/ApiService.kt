package com.notsatria.storyapp.data.remote.retrofit

import com.notsatria.storyapp.data.remote.response.ApiResponse
import com.notsatria.storyapp.data.remote.response.UserLoginResponse
import retrofit2.Call
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    fun register(name: String, email: String, password: String): Call<ApiResponse>

    @POST("login")
    fun login(name: String, email: String, password: String): Call<UserLoginResponse>

}