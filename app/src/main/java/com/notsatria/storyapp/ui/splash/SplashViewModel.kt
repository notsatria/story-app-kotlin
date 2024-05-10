package com.notsatria.storyapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.notsatria.storyapp.data.preferences.UserPreference

class SplashViewModel(private val userPreference: UserPreference) : ViewModel() {
    fun getUserLoginStatus(): LiveData<Boolean> {
        return userPreference.getUserLoginStatus().asLiveData()
    }
}