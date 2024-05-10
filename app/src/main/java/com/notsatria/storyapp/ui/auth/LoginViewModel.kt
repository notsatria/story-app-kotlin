package com.notsatria.storyapp.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notsatria.storyapp.data.Result
import com.notsatria.storyapp.data.model.User
import com.notsatria.storyapp.data.remote.response.RegisterResponse
import com.notsatria.storyapp.data.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val result = MediatorLiveData<Result<User>>()

    fun login(
        email: String,
        password: String
    ): LiveData<Result<User>> {
        viewModelScope.launch {
            result.value = Result.Loading
            try {
                val response = authRepository.login(email, password)
                if (response.error == false) {
                    val loginResult = response.loginResult
                    val user = User(
                        id = loginResult?.userId!!,
                        name = loginResult.name!!,
                        token = loginResult.token!!,
                        isLoggedIn = true
                    )
                    result.value = Result.Success(user)
                } else {
                    Log.e("LoginViewModel", "Error: ${response.message}")
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