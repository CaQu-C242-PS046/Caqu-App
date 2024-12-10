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
        private const val LAST_LOGIN_TIME = "lastLoginTime" // Contoh tambahan untuk waktu login terakhir
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
