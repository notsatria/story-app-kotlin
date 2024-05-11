package com.notsatria.storyapp.di

import android.content.Context
import com.notsatria.storyapp.data.preferences.UserPreference
import com.notsatria.storyapp.data.remote.retrofit.ApiConfig
import com.notsatria.storyapp.data.repository.AuthRepository
import com.notsatria.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    private fun getToken(context: Context): String {
        val pref = UserPreference.getInstance(context)
        return runBlocking { pref.getTokenValue().first() }
    }

    fun provideAuthRepository(context: Context): AuthRepository {
        val token = getToken(context)
        return AuthRepository.getInstance(apiService = ApiConfig.getApiService(context, token))
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val token = getToken(context)
        return StoryRepository.getInstance(apiService = ApiConfig.getApiService(context, token))
    }

    fun provideUserPreference(context: Context): UserPreference {
        return UserPreference.getInstance(context)
    }
}