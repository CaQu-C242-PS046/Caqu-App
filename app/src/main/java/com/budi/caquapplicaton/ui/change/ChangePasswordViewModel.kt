package com.budi.caquapplicaton.ui.change

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budi.caquapplicaton.retrofit.ApiClient
import com.budi.caquapplicaton.retrofit.AuthService
import com.budi.caquapplicaton.retrofit.ChangePasswordRequest
import com.budi.caquapplicaton.retrofit.ChangePasswordResponse
import com.budi.caquapplicaton.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch
import retrofit2.Response

class ChangePasswordViewModel(context: Context) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _responseMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String> get() = _responseMessage

    private val apiService = ApiClient.getClient().create(AuthService::class.java)
    private val sharedPreferencesHelper = SharedPreferencesHelper(context)

    // Fungsi untuk mengganti password
    fun changePassword(oldPassword: String, newPassword: String) {
        _isLoading.value = true

        // Ambil token akses dari SharedPreferences
        val token = sharedPreferencesHelper.getAccessToken()

        // Pastikan token ada
        if (token.isNullOrEmpty()) {
            _responseMessage.value = "Token not found. Please log in again."
            _isLoading.value = false
            return
        }

        // Siapkan request untuk perubahan password
        val request = ChangePasswordRequest(oldPassword, newPassword)

        // Memulai coroutine untuk melakukan operasi asinkron
        viewModelScope.launch {
            try {
                // Panggil API menggunakan suspend function
                val response: Response<ChangePasswordResponse> = apiService.changePassword("Bearer $token", request)

                // Cek response dari server
                if (response.isSuccessful) {
                    _responseMessage.value = response.body()?.message ?: "Password successfully changed."
                } else {
                    _responseMessage.value = "Failed to change password. Error code: ${response.code()}"
                }
            } catch (e: Exception) {
                // Tangani error yang terjadi
                _responseMessage.value = "An error occurred: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
