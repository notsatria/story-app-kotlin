package com.notsatria.storyapp.di

import android.content.Context
import com.notsatria.storyapp.data.remote.retrofit.ApiConfig
import com.notsatria.storyapp.data.repository.AuthRepository

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        return AuthRepository.getInstance(apiService = ApiConfig.getApiService(context))
    }
}