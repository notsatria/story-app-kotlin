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
import com.notsatria.storyapp.ui.main.ui.addstory.AddStoryViewModel
import com.notsatria.storyapp.ui.main.ui.home.DetailStoryViewModel
import com.notsatria.storyapp.ui.main.ui.home.HomeViewModel
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
            DetailStoryViewModel::class.java -> DetailStoryViewModel(storyRepository, userPreference) as T
            AddStoryViewModel::class.java -> AddStoryViewModel() as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                ViewModelFactory(
                    Injection.provideAuthRepository(context),
                    Injection.provideStoryRepository(context),
                    Injection.provideUserPreference(context)
                ).also { INSTANCE = it }
            }
        }
    }
}