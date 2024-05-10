package com.notsatria.storyapp.data.repository

import com.notsatria.storyapp.data.remote.response.DetailStoryResponse
import com.notsatria.storyapp.data.remote.response.FetchStoriesResponse
import com.notsatria.storyapp.data.remote.retrofit.ApiService

class StoryRepository private constructor(private val apiService: ApiService) {

    suspend fun fetchAllStories(token: String): FetchStoriesResponse {
        return apiService.fetchAllStories("Bearer $token")
    }

    suspend fun getStoryDetail(token: String, id: String): DetailStoryResponse {
        return apiService.getDetailStory("Bearer $token", id)
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}