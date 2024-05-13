package com.notsatria.storyapp.ui.main.ui.addstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.notsatria.storyapp.data.Result
import com.notsatria.storyapp.data.remote.response.ErrorResponse
import com.notsatria.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class AddStoryViewModel(private val storyRepository: StoryRepository) :
    ViewModel() {

    private val result = MediatorLiveData<Result<ErrorResponse>>()

    fun postStory(description: String, imageFile: File): LiveData<Result<ErrorResponse>> {
        viewModelScope.launch {
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            try {
                result.value = Result.Loading
                val response = storyRepository.postStory(requestBody, multipartBody)
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

    companion object {
        private const val TAG = "AddStoryViewModel"
    }
}