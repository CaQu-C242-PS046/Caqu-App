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

data class ForgotPasswordResponse(
    val message: String,
    val success: Boolean,
    val resetLink: String? = null
)

data class RefreshTokenResponse(
    val message: String,
    val success: Boolean,
    val accessToken: String
)

data class VerifyTokenResponse(
    val valid: Boolean,
    val message: String,
    val email: String? = null
)


