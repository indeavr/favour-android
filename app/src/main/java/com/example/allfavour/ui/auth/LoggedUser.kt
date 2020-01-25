package com.example.allfavour.ui.auth

data class LoggedUser(
    val userId: String,
    val email: String,
    val token: String
)