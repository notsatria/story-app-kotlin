package com.notsatria.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.notsatria.storyapp.data.paging.StoryPagingSource
import com.notsatria.storyapp.data.remote.response.DetailStoryResponse
import com.notsatria.storyapp.data.remote.response.ErrorResponse
import com.notsatria.storyapp.data.remote.response.StoryItem
import com.notsatria.storyapp.data.remote.response.StoryResponse
import com.notsatria.storyapp.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(private val apiService: ApiService) {

    fun getStories(): LiveData<PagingData<StoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    suspend fun fetchAllStories(): StoryResponse {
        return apiService.fetchAllStories()
    }

    suspend fun getStoryDetail(id: String): DetailStoryResponse {
        return apiService.getDetailStory(id)
    }

    suspend fun postStory(description: RequestBody, file: MultipartBody.Part): ErrorResponse {
        return apiService.postStory(description, file)
    }

    suspend fun getStoriesWithLocation(): StoryResponse {
        return apiService.getStoriesWithLocation()
    }

    companion object {
        fun getInstance(
            apiService: ApiService
        ): StoryRepository =
            StoryRepository(apiService)
    }
}