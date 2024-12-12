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


@Parcelize
data class VideoDetail(
    val videoLink: String,
    val title: String,
    val description: String,
    val thumbnails: Thumbnails
) : Parcelable


@Parcelize
data class Thumbnails(
    val high: ThumbnailDetail,
    val default: ThumbnailDetail
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
    val predicted_career: String
)




data class ErrorResponse(
    val error: Boolean,
    val errorMessage: String
)


data class QuizQuestionsResponse(
    val success: Boolean,
    val message: String,
    val questions: List<QuestionItem>
)


data class QuestionItem(
    val questionId: Int,
    val questionText: String,
    val options: List<String>
)


data class SubmitQuizResponse(
    val success: Boolean,
    val message: String,
    val recommendedCareer: String
)


data class QuestionStatus(
    val questionId: Int,
    val questionText: String,
    val answered: Boolean
)

@Parcelize
data class VideoPlaylist(
    val videoLink: String,
    val title: String,
    val thumbnails: Thumbnails
) : Parcelable

@Parcelize
data class VideoFeedback(
    val videoLink: String,
    val title: String,
    val thumbnails: Thumbnails
) : Parcelable


data class CareerResponse(
    val namaKarir: String,
    val skill: List<String>,
    val pendidikan: List<String>,
    val insight: List<String>,
    val image: String,
    val feedback: Feedback,
    val video: List<VideoCareer>
)

data class VideoCareer(
    val snippet : Snippet
)

data class Snippet(
    val playlistLink: String,
    val title: String,
    val thumbnails: Thumbnails
)

@Parcelize
data class Feedback(
    val thumbnails: Thumbnails,
    val title: String,
    val videoLink: String
) : Parcelable



