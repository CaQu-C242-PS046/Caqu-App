package com.budi.caquapplicaton.retrofit

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class ResetPasswordRequest(
    val email: String
)

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)

