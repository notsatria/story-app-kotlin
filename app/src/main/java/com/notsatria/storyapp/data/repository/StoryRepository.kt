package com.notsatria.storyapp.data.repository

import com.notsatria.storyapp.data.remote.response.DetailStoryResponse
import com.notsatria.storyapp.data.remote.response.ErrorResponse
import com.notsatria.storyapp.data.remote.response.FetchStoriesResponse
import com.notsatria.storyapp.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(private val apiService: ApiService) {

    suspend fun fetchAllStories(): FetchStoriesResponse {
        return apiService.fetchAllStories()
    }

    suspend fun getStoryDetail(id: String): DetailStoryResponse {
        return apiService.getDetailStory(id)
    }

    suspend fun postStory(description: RequestBody, file: MultipartBody.Part): ErrorResponse {
        return apiService.postStory(description, file)
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