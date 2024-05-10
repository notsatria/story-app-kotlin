package com.notsatria.storyapp.ui.main.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.notsatria.storyapp.data.Result
import com.notsatria.storyapp.data.preferences.UserPreference
import com.notsatria.storyapp.data.remote.response.FetchStoriesResponse
import com.notsatria.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(
    private val storyRepository: StoryRepository,
    private val userPreference: UserPreference
) : ViewModel() {

    private val result = MediatorLiveData<Result<FetchStoriesResponse>>()
    private val TAG = "HomeViewModel"

    fun fetchAllStories(token: String): LiveData<Result<FetchStoriesResponse>> {
        viewModelScope.launch {
            try {
                result.value = Result.Loading
                val response = storyRepository.fetchAllStories(token)
                if (response.error == false) {
                    Log.d(TAG, "Message: ${response.message}")
                    result.value = Result.Success(response)
                } else {
                    Log.e(TAG, "Error: ${response.message}")
                    result.value = Result.Error(response.message!!)
                }
            } catch (e: HttpException) {
                val errorMessage = "HTTP error: ${e.code()} ${e.message()}"
                result.value = Result.Error(errorMessage)
            } catch (e: Exception) {
                e.printStackTrace()
                result.value = Result.Error(e.message.toString())
            }
        }
        return result
    }

    fun getToken(): LiveData<String> {
        return userPreference.getTokenValue().asLiveData()
    }

    fun clearAllSession() {
        viewModelScope.launch {
            userPreference.apply {
                setTokenValue("")
                setUserLoginStatus(false)
            }
        }
    }
}