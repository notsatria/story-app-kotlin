package com.notsatria.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.notsatria.storyapp.data.Result
import com.notsatria.storyapp.data.remote.response.ErrorResponse
import com.notsatria.storyapp.data.remote.response.StoryResponse
import com.notsatria.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val result = MediatorLiveData<Result<StoryResponse>>(Result.Loading)

    fun getStoriesWithLocation(): LiveData<Result<StoryResponse>> {
        viewModelScope.launch {
            try {
                val response = storyRepository.getStoriesWithLocation()
                if (response.error == false) {
                    result.value = Result.Success(response)
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
}