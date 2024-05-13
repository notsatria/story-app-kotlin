package com.notsatria.storyapp.data.repository

import com.notsatria.storyapp.data.remote.response.LoginResponse
import com.notsatria.storyapp.data.remote.response.RegisterResponse
import com.notsatria.storyapp.data.remote.retrofit.ApiService


class AuthRepository private constructor(private val apiService: ApiService) {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(name: String, password: String): LoginResponse {
        return apiService.login(name, password)
    }

    companion object {
        private const val TAG = "AuthRepository"

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