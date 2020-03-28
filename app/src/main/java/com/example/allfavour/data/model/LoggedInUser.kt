package com.example.allfavour.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userId: String,
    val displayName: String,
    val token: String,
    val fullName: String,
    val permissions: PermissionsModel,
    val lastAccountSide: String?
)
