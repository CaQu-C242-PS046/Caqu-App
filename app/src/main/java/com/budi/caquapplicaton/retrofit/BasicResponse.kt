package com.budi.caquapplicaton.retrofit

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

// Response untuk daftar soft skills
data class ResponseSoftSkills(
    val data: List<String>
)

// Detail soft skill
@Parcelize
data class SoftSkillDetail(
    val nama_ss: String,
    val video: VideoDetail
) : Parcelable

// Detail video
@Parcelize
data class VideoDetail(
    val videoLink: String,
    val title: String,
    val description: String,
    val thumbnails: Thumbnails
) : Parcelable

// Thumbnails dari video
@Parcelize
data class Thumbnails(
    val high: ThumbnailDetail
) : Parcelable

// Detail thumbnail
@Parcelize
data class ThumbnailDetail(
    val url: String
) : Parcelable

// Response untuk mendapatkan pertanyaan
data class QuestionResponse(
    val question: String,
    val questionNumber: Int
)

// Response generik (umum) untuk submit jawaban
data class GenericResponse(
    val success: Boolean,
    val message: String
)

// Response untuk status kuis
data class QuizStatusResponse(
    val success: Boolean,
    val message: String,
    val quizStatus: List<QuizStatusItem>
)

// Item status kuis
data class QuizStatusItem(
    val questionId: Int,
    val questionText: String,
    val answered: Boolean
)

// Response untuk rekomendasi karir
data class RecommendationResponse(
    val success: Boolean,
    val message: String,
    val recommendedCareer: String
)

// Tambahan untuk respon lainnya:

// Response jika ada error atau kegagalan
data class ErrorResponse(
    val error: Boolean,
    val errorMessage: String
)

// Response untuk daftar pertanyaan dalam kuis
data class QuizQuestionsResponse(
    val success: Boolean,
    val message: String,
    val questions: List<QuestionItem>
)

// Item pertanyaan dalam daftar kuis
data class QuestionItem(
    val questionId: Int,
    val questionText: String,
    val options: List<String> // Jika ada opsi jawaban
)

// Response untuk validasi atau pengiriman jawaban akhir kuis
data class SubmitQuizResponse(
    val success: Boolean,
    val message: String,
    val recommendedCareer: String // Rekomendasi karir berdasarkan jawaban
)


data class QuestionStatus(
    val questionId: Int,
    val questionText: String,
    val answered: Boolean
)
