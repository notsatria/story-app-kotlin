package com.notsatria.storyapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.gson.Gson
import com.notsatria.storyapp.data.Result
import com.notsatria.storyapp.data.paging.StoryPagingSource
import com.notsatria.storyapp.data.preferences.UserPreference
import com.notsatria.storyapp.data.remote.response.ErrorResponse
import com.notsatria.storyapp.data.remote.response.StoryItem
import com.notsatria.storyapp.data.remote.response.StoryResponse
import com.notsatria.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(
    private val storyRepository: StoryRepository,
    private val userPreference: UserPreference
) : ViewModel() {

    private val result = MediatorLiveData<Result<StoryResponse>>()

    val stories: LiveData<PagingData<StoryItem>> = storyRepository.getStories().cachedIn(viewModelScope)

    fun clearAllSession() {
        viewModelScope.launch {
            userPreference.apply {
                setTokenValue("")
                setUserLoginStatus(false)
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}