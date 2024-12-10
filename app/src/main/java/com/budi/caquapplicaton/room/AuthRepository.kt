package com.budi.caquapplicaton.room


import com.budi.caquapplicaton.retrofit.AuthService
import com.budi.caquapplicaton.retrofit.RefreshTokenRequest
import com.budi.caquapplicaton.utils.SharedPreferencesHelper

class AuthRepository(
    private val authService: AuthService,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) {
    suspend fun refreshAccessToken(): String? {
        val refreshToken = sharedPreferencesHelper.getRefreshToken()

        if (refreshToken.isNullOrEmpty()) {
            return null // Tidak ada refresh token tersimpan
        }

        val response = authService.refreshAuthToken(RefreshTokenRequest(refreshToken))
        return if (response.isSuccessful) {
            val newAccessToken = response.body()?.accessToken
            if (!newAccessToken.isNullOrEmpty()) {
                sharedPreferencesHelper.saveTokens(newAccessToken, refreshToken) // Simpan token baru
            }
            newAccessToken
        } else {
            null // Token tidak dapat diperbarui
        }
    }
}
