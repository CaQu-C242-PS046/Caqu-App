package com.budi.caquapplicaton.utils

import android.content.Context
import android.content.SharedPreferences
import com.budi.caquapplicaton.retrofit.QuizStatusItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val ACCESS_TOKEN = "accessToken"
        private const val REFRESH_TOKEN = "refreshToken"
        private const val IS_LOGGED_IN = "isLoggedIn"
        private const val USERNAME = "username"
        private const val LAST_LOGIN_TIME = "lastLoginTime"
        private const val LAST_RECOMMENDATION = "lastRecommendation"
        private const val QUIZ_STATUS = "quizStatus"
    }

    // Simpan token akses dan token refresh
    fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString(ACCESS_TOKEN, accessToken)
            putString(REFRESH_TOKEN, refreshToken)
            putBoolean(IS_LOGGED_IN, true)
            apply()
        }
    }

    // Simpan nama pengguna
    fun saveUsername(username: String) {
        sharedPreferences.edit().apply {
            putString(USERNAME, username)
            apply()
        }
    }

    fun saveLastRecommendation(recommendation: String) {
        sharedPreferences.edit().apply {
            putString(LAST_RECOMMENDATION, recommendation)
            apply()
        }
    }

    fun saveQuizStatus(quizStatus: List<QuizStatusItem>) {
        val gson = Gson()
        val json = gson.toJson(quizStatus)
        sharedPreferences.edit().apply {
            putString(QUIZ_STATUS, json)
            apply()
        }
    }

    fun getQuizStatus(): List<QuizStatusItem>? {
        val gson = Gson()
        val json = sharedPreferences.getString(QUIZ_STATUS, null)
        return if (json != null) {
            val type = object : TypeToken<List<QuizStatusItem>>() {}.type
            gson.fromJson(json, type)
        } else {
            null
        }
    }

    // Simpan waktu login terakhir (opsional)
    fun saveLastLoginTime(timestamp: Long) {
        sharedPreferences.edit().apply {
            putLong(LAST_LOGIN_TIME, timestamp)
            apply()
        }
    }

    // Ambil token akses
    fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN, null)
    }

    // Ambil token refresh
    fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN, null)
    }

    // Ambil nama pengguna
    fun getUsername(): String? {
        return sharedPreferences.getString(USERNAME, null)
    }

    fun getLastRecommendation(): String? {
        return sharedPreferences.getString(LAST_RECOMMENDATION, null)
    }

    // Ambil waktu login terakhir
    fun getLastLoginTime(): Long {
        return sharedPreferences.getLong(LAST_LOGIN_TIME, 0L)
    }

    // Periksa apakah pengguna telah login
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    // Hapus semua data pengguna
    fun clearUserData() {
        sharedPreferences.edit().apply {
            clear()
            apply()
        }
    }

    // Logout pengguna dengan menghapus status login dan token
    fun logout() {
        sharedPreferences.edit().apply {
            remove(ACCESS_TOKEN)
            remove(REFRESH_TOKEN)
            putBoolean(IS_LOGGED_IN, false)
            apply()
        }
    }
}
