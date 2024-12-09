package com.budi.caquapplicaton.retrofit

data class RegisterResponse(
    val message: String,
    val success: Boolean,
)

data class LoginResponse(
    val message: String,
    val success: Boolean,
    val accessToken: String,
    val refreshToken: String
)

data class ResetPasswordResponse(
    val message: String
)

data class ChangePasswordResponse(
    val message: String
)


