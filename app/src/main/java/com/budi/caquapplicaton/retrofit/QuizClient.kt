package com.budi.caquapplicaton.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object QuizClient {
    private const val BASE_URL = "https://caqu-app-442406.et.r.appspot.com/"

    val instance: BaseService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BaseService::class.java)
    }
}
