package com.budi.caquapplicaton.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CareerClient {
    private const val BASE_URL = "https://caqu-app-442406.et.r.appspot.com/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}
