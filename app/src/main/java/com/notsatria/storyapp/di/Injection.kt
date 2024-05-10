package com.notsatria.storyapp.di

import android.content.Context
import com.notsatria.storyapp.data.preferences.UserPreference
import com.notsatria.storyapp.data.remote.retrofit.ApiConfig
import com.notsatria.storyapp.data.repository.AuthRepository
import com.notsatria.storyapp.data.repository.StoryRepository

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        return AuthRepository.getInstance(apiService = ApiConfig.getApiService(context))
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        return StoryRepository.getInstance(apiService = ApiConfig.getApiService(context))
    }

    fun provideUserPreference(context: Context): UserPreference {
        return UserPreference.getInstance(context)
    }
}