package com.budi.caquapplicaton.retrofit


data class Video(
    val playlistUrl: String,
    val thumbnailUrl: String,
    val videoUrl: String
)

data class AnswerRequest(
    val question_id: Int,
    val answer: String
)