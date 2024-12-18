package com.budi.caquapplicaton.ui.career

import com.budi.caquapplicaton.retrofit.AuthService
import com.budi.caquapplicaton.retrofit.CareerResponse
import retrofit2.Response

class CareerRepository(private val apiService: AuthService) {
    suspend fun getCareerDetails(name: String, token: String): Response<CareerResponse> {
        return apiService.getCareerDetail(name, token)
    }
}
