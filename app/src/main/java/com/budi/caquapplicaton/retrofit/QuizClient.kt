package com.budi.caquapplicaton.retrofit

import retrofit2.Retrofit

object QuizClient {
    private const val BASE_URL = "https://caqu-app-442406.et.r.appspot.com/"

    private fun getClient(): Retrofit {
        return BaseClient.getClient()
    }

    fun getQuizApi(): BaseService {
        return getClient().create(BaseService::class.java)
    }
}
