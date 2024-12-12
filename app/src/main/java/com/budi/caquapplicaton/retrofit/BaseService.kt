package com.budi.caquapplicaton.retrofit

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface BaseService {


    @GET("softSkills/all")
    suspend fun getSoftSkillNames(
        @Header("Authorization") token: String
    ): Response<ResponseSoftSkills>

    @POST("/auth/refresh")
    suspend fun refreshAuthToken(
        @Body refreshTokenRequest: RefreshTokenRequest
    ): Response<RefreshTokenResponse>

    @POST("/auth/login")
    suspend fun login(
        @Body refreshToken: String
    ): Response<LoginResponse>


    @GET("softSkills/{name}")
    suspend fun getSoftSkillDetail(
        @Path("name", encoded = true) name: String,
        @Header("Authorization") token: String
    ): Response<SoftSkillDetail>


    @GET("/quiz/question/{number}")
    fun getQuestion(
        @Path("number") number: Int,
        @Header("Authorization") token: String
    ): Call<QuestionResponse>


    @POST("/quiz/answer")
    fun submitAnswer(
        @Body answer: AnswerRequest,
        @Header("Authorization") token: String
    ): Call<GenericResponse>


    @GET("/quiz/quiz-status")
    fun getQuizStatus(
        @Header("Authorization") token: String
    ): Call<QuizStatusResponse>


    @POST("/quiz/submitQuiz")
    fun submitQuiz(
        @Header("Authorization") token: String
    ): Call<RecommendationResponse>

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
