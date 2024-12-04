package com.budi.caquapplicaton.retrofit

data class SoftSkillDetail(
    val nama_ss: String,
    val artikel: List<String>,
    val video: Video
)

data class Video(
    val playlistUrl: String,
    val thumbnailUrl: String,
    val videoUrl: String
)

data class AnswerRequest(
    val question_id: Int,
    val answer: String
)