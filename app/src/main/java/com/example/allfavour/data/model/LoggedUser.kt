package com.example.allfavour.data.model

data class LoggedUser(
    val userId: String,
    val email: String,
    val token: String,
    val fullName: String,
    val permissions: PermissionsModel,
    val lastAccountSide: String?
)