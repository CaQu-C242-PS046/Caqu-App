package com.budi.caquapplicaton.retrofit

data class ResponseSoftSkills(
    val data: List<String>
)

data class QuestionResponse(
    val question: String,
    val questionNumber: Int
)


data class GenericResponse(
    val success: Boolean,
    val message: String
)

data class QuizStatusResponse(
    val success: Boolean,
    val message: String,
    val quizStatus: List<QuizStatusItem>
)

data class QuizStatusItem(
    val questionId: Int,
    val questionText: String,
    val answered: Boolean
)

data class RecommendationResponse(
    val message: String,
    val recommendedCareer: String
)
