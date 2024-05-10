package com.notsatria.storyapp.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
class UserPreference private constructor(private val dataStore: DataStore<Preferences>){

    private val TOKEN_KEY = stringPreferencesKey("token")
    private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")

    fun getTokenValue(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    suspend fun setTokenValue(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    fun getUserLoginStatus(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_LOGGED_IN_KEY] ?: false
        }
    }

    suspend fun setUserLoginStatus(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = isLoggedIn
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        fun getInstance(context: Context): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val dataStore = context.dataStore
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}