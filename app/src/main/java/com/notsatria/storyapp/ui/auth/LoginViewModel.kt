package com.notsatria.storyapp.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.notsatria.storyapp.data.Result
import com.notsatria.storyapp.data.model.User
import com.notsatria.storyapp.data.preferences.UserPreference
import com.notsatria.storyapp.data.remote.response.ErrorResponse
import com.notsatria.storyapp.data.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userPreference: UserPreference
) : ViewModel() {

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
                    userPreference.setTokenValue(loginResult.token!!)
                    userPreference.setUserLoginStatus(user.isLoggedIn)
                    Log.d(TAG, "Token: ${user.token}")
                    result.value = Result.Success(user)
                } else {
                    Log.e("LoginViewModel", "Error: ${response.message}")
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
        private const val TAG = "LoginViewModel"
    }
}