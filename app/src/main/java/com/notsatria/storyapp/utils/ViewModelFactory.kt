package com.notsatria.storyapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.notsatria.storyapp.data.preferences.UserPreference
import com.notsatria.storyapp.data.repository.AuthRepository
import com.notsatria.storyapp.data.repository.StoryRepository
import com.notsatria.storyapp.di.Injection
import com.notsatria.storyapp.ui.auth.LoginViewModel
import com.notsatria.storyapp.ui.auth.RegisterViewModel
import com.notsatria.storyapp.ui.addstory.AddStoryViewModel
import com.notsatria.storyapp.ui.home.DetailStoryViewModel
import com.notsatria.storyapp.ui.home.HomeViewModel
import com.notsatria.storyapp.ui.home.MapsViewModel
import com.notsatria.storyapp.ui.settings.SettingsViewModel
import com.notsatria.storyapp.ui.splash.SplashViewModel

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository,
    private val userPreference: UserPreference,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            RegisterViewModel::class.java -> RegisterViewModel(authRepository) as T
            LoginViewModel::class.java -> LoginViewModel(authRepository, userPreference) as T
            SplashViewModel::class.java -> SplashViewModel(userPreference) as T
            HomeViewModel::class.java -> HomeViewModel(storyRepository, userPreference) as T
            MapsViewModel::class.java -> MapsViewModel(storyRepository) as T
            DetailStoryViewModel::class.java -> DetailStoryViewModel(
                storyRepository,
                userPreference
            ) as T
            AddStoryViewModel::class.java -> AddStoryViewModel(storyRepository) as T
            SettingsViewModel::class.java -> SettingsViewModel(userPreference) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        fun getInstance(context: Context): ViewModelFactory =
            ViewModelFactory(
                Injection.provideAuthRepository(context),
                Injection.provideStoryRepository(context),
                Injection.provideUserPreference(context)
            )
    }
}