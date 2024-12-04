package com.budi.caquapplicaton.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val ACCESS_TOKEN = "accessToken"
        private const val REFRESH_TOKEN = "refreshToken"
        private const val IS_LOGGED_IN = "isLoggedIn"
        private const val USERNAME = "username"
    }

    // Simpan token dan status login
    fun saveTokens(accessToken: String, refreshToken: String) {
        val editor = sharedPreferences.edit()
        editor.putString(ACCESS_TOKEN, accessToken)
        editor.putString(REFRESH_TOKEN, refreshToken)
        editor.putBoolean(IS_LOGGED_IN, true)
        editor.apply()
    }

    // Simpan username
    fun saveUsername(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString(USERNAME, username)
        editor.apply()
    }

    // Ambil token akses
    fun getAccessToken(): String? = sharedPreferences.getString(ACCESS_TOKEN, null)

    // Ambil token refresh
    fun getRefreshToken(): String? = sharedPreferences.getString(REFRESH_TOKEN, null)

    // Ambil username
    fun getUsername(): String? = sharedPreferences.getString(USERNAME, null)

    // Periksa status login
    fun isLoggedIn(): Boolean = sharedPreferences.getBoolean(IS_LOGGED_IN, false)

    // Hapus data pengguna
    fun clearUserData() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
