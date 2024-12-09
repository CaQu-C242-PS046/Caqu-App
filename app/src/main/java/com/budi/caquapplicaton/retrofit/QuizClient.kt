package com.budi.caquapplicaton.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object QuizClient {
    private const val BASE_URL = "https://caqu-app-442406.et.r.appspot.com/"

    private fun getClient(): Retrofit {
        return BaseClient.getClient() // Memanfaatkan BaseClient yang sudah ada
    }

    fun getQuizApi(): BaseService {
        return getClient().create(BaseService::class.java)
    }
}
