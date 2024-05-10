package com.notsatria.storyapp.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notsatria.storyapp.data.Result
import com.notsatria.storyapp.data.remote.response.RegisterResponse
import com.notsatria.storyapp.data.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val result = MediatorLiveData<Result<RegisterResponse>>()

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> {
        viewModelScope.launch {
            result.value = Result.Loading
            try {
                val response = authRepository.register(name, email, password)
                if (response.error == false) {
                    result.value = Result.Success(response)
                } else {
                    Log.e("RegisterViewModel", "Error: ${response.message}")
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

}