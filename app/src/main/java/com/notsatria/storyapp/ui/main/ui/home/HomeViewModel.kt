package com.notsatria.storyapp.ui.main.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.notsatria.storyapp.data.Result
import com.notsatria.storyapp.data.preferences.UserPreference
import com.notsatria.storyapp.data.remote.response.ErrorResponse
import com.notsatria.storyapp.data.remote.response.FetchStoriesResponse
import com.notsatria.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(
    private val storyRepository: StoryRepository,
    private val userPreference: UserPreference
) : ViewModel() {

    private val result = MediatorLiveData<Result<FetchStoriesResponse>>()

    fun fetchAllStories(): LiveData<Result<FetchStoriesResponse>> {
        viewModelScope.launch {
            try {
                result.value = Result.Loading
                val response = storyRepository.fetchAllStories()
                if (response.error == false) {
                    Log.d(TAG, "Message: ${response.message}")
                    result.value = Result.Success(response)
                } else {
                    Log.e(TAG, "Error: ${response.message}")
                    result.value = Result.Error(response.message!!)
                }
            } catch (e: HttpException) {
                val jsonString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                result.value = Result.Error(errorMessage!!)
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

    companion object {
        private const val TAG = "HomeViewModel"
    }
}