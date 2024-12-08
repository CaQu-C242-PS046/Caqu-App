package com.budi.caquapplicaton.retrofit
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

data class ResponseSoftSkills(
    val data: List<String>
)

@Parcelize
data class SoftSkillDetail(
    val nama_ss: String,
    val video: VideoDetail
) : Parcelable

@Parcelize
data class VideoDetail(
    val videoLink: String,
    val title: String,
    val description: String,
    val thumbnails: Thumbnails
) : Parcelable

@Parcelize
data class Thumbnails(
    val high: ThumbnailDetail
) : Parcelable

@Parcelize
data class ThumbnailDetail(
    val url: String
) : Parcelable


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
