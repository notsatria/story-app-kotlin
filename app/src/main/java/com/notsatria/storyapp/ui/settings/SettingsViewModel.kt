package com.notsatria.storyapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notsatria.storyapp.data.preferences.UserPreference
import kotlinx.coroutines.launch

class SettingsViewModel(private val userPreference: UserPreference) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            userPreference.apply {
                setTokenValue("")
                setUserLoginStatus(false)
            }
        }
    }
}