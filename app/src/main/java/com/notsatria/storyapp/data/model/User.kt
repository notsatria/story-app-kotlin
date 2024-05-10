package com.notsatria.storyapp.data.model

data class User(
    val id: String,
    val name: String,
    val token: String,
    val isLoggedIn: Boolean,
)
