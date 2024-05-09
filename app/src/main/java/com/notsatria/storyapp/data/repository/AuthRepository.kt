package com.notsatria.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.notsatria.storyapp.data.model.User
import com.notsatria.storyapp.data.remote.retrofit.ApiService
import com.notsatria.storyapp.data.Result
import com.notsatria.storyapp.data.remote.response.ApiResponse
import com.notsatria.storyapp.data.remote.response.UserLoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthRepository private constructor(private val apiService: ApiService) {

    private val resultRegister = MediatorLiveData<Result<ApiResponse>>()
    private val resultLogin = MediatorLiveData<Result<User>>()

    fun register(name: String, email: String, password: String): LiveData<Result<ApiResponse>> {
        resultRegister.value = Result.Loading
        val client = apiService.register(name, email, password)
        client.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    resultRegister.value = Result.Success<ApiResponse>(
                        ApiResponse(
                            responseBody?.error,
                            responseBody?.message
                        )
                    )
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                resultRegister.value = Result.Empty(t.message.toString())
            }

        })

        return resultRegister
    }

    fun login(email: String, password: String): LiveData<Result<User>> {
        resultLogin.value = Result.Loading
        val client = apiService.login(email, password)
        client.enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(
                call: Call<UserLoginResponse>,
                response: Response<UserLoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.error == false) {
                        val loginResult = responseBody?.loginResult
                        val user = User(
                            id = loginResult?.userId!!,
                            name = loginResult?.name!!,
                            token = loginResult?.token!!
                        )
                        resultLogin.value = Result.Success<User>(user)
                    } else {
                        resultLogin.value = Result.Error(responseBody?.message!!)
                    }

                }
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                resultLogin.value = Result.Error(t.message.toString())
            }

        })

        return resultLogin
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService)
            }.also { instance = it }
    }
}