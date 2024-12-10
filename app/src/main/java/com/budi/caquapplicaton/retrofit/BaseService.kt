package com.budi.caquapplicaton.retrofit

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface BaseService {

    // Mendapatkan daftar nama soft skills
    @GET("softSkills/all")
    suspend fun getSoftSkillNames(
        @Header("Authorization") token: String // Menambahkan parameter token
    ): Response<ResponseSoftSkills>

    @POST("/auth/login")
    suspend fun login(
        @Body refreshToken: String // Refresh token yang sudah ada
    ): Response<LoginResponse>

    // Mendapatkan detail soft skill berdasarkan nama
    @GET("softSkills/{name}")
    suspend fun getSoftSkillDetail(
        @Path("name", encoded = true) name: String, // Parameter nama soft skill
        @Header("Authorization") token: String // Menambahkan parameter token
    ): Response<SoftSkillDetail>

    // Mendapatkan pertanyaan berdasarkan nomor
    @GET("/quiz/question/{number}")
    fun getQuestion(
        @Path("number") number: Int,
        @Header("Authorization") token: String // Menambahkan parameter token
    ): Call<QuestionResponse>

    // Mengirimkan jawaban untuk pertanyaan tertentu
    @POST("/quiz/answer")
    fun submitAnswer(
        @Body answer: AnswerRequest,
        @Header("Authorization") token: String // Menambahkan parameter token
    ): Call<GenericResponse>

    // Mendapatkan status kuis
    @GET("/quiz/quiz-status")
    fun getQuizStatus(
        @Header("Authorization") token: String // Menambahkan parameter token
    ): Call<QuizStatusResponse>

    // Menyelesaikan kuis dan mendapatkan rekomendasi
    @POST("/quiz/submitQuiz")
    fun submitQuiz(
        @Header("Authorization") token: String // Menambahkan parameter token
    ): Call<RecommendationResponse>

    @GET("career/{name}")
    suspend fun getCareerDetail(
        @Path("name", encoded = true) name: String,
        @Header("Authorization") token: String
    ): Response<CareerResponse>


}
