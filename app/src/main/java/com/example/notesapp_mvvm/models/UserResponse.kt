package com.example.notesapp_mvvm.models

data class UserResponse(
    val status: Boolean,
    val token: String,
    val user: User
)