package com.budi.caquapplicaton.retrofit
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthService {

    @POST("/auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/auth/login")
    suspend fun login(
        @Body refreshToken: String
    ): Response<LoginResponse>

    @POST("/auth/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/auth/change")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body request: ChangePasswordRequest
    ): Response<ChangePasswordResponse>

    @POST("/auth/refresh")
    suspend fun refreshAuthToken(
        @Body refreshTokenRequest: RefreshTokenRequest
    ): Response<RefreshTokenResponse>

    @POST("/forgot-password")
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<ForgotPasswordResponse>

    @PUT("/reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<ResetPasswordResponse>

    @GET("career/{name}")
    suspend fun getCareerDetail(
        @Path("name", encoded = true) name: String,
        @Header("Authorization") token: String
    ): Response<CareerResponse>

    @GET("/history")
    suspend fun getCareerName(
        @Header("Authorization") token: String,
    ): Response<CareerNameResponse>
}

