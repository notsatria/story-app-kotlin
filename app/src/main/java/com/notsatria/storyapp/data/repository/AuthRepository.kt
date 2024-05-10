package com.notsatria.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.notsatria.storyapp.data.Result
import com.notsatria.storyapp.data.model.User
import com.notsatria.storyapp.data.remote.response.RegisterResponse
import com.notsatria.storyapp.data.remote.response.LoginResponse
import com.notsatria.storyapp.data.remote.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthRepository private constructor(private val apiService: ApiService) {

    private val TAG = "AuthRepository"

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(name: String, password: String): LoginResponse {
        return apiService.login(name, password)
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService)
            }.also { instance = it }
    }
}