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
    val email: String,
    val newPassword: String
)

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)

data class ForgotPasswordRequest(
    val email: String
)

data class RefreshTokenRequest(
    val refreshToken: String,
)

