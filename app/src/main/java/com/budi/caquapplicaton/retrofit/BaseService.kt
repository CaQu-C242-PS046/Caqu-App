package com.budi.caquapplicaton.retrofit

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
interface BaseService {
    // Mendapatkan daftar nama soft skills
    @GET("softSkills/all")
    suspend fun getSoftSkillNames(): ResponseSoftSkills

    // Mendapatkan detail soft skill berdasarkan nama
    @GET("softSkills/{name}")
    suspend fun getSoftSkillDetail(@Path("name") name: String): SoftSkillDetail

    @GET("question/{number}")
    fun getQuestion(
        @Path("number") number: Int
    ): Call<QuestionResponse>

    @POST("answer")
    fun submitAnswer(
        @Body answer: AnswerRequest
    ): Call<GenericResponse>

    @GET("quiz-status")
    fun getQuizStatus(): Call<QuizStatusResponse>

    @POST("submit")
    fun submitQuiz(): Call<RecommendationResponse>
}
