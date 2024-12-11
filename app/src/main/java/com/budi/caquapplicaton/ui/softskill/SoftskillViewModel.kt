package com.budi.caquapplicaton.ui.softskill

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.budi.caquapplicaton.retrofit.ApiClient
import com.budi.caquapplicaton.retrofit.AuthService
import com.budi.caquapplicaton.retrofit.BaseClient
import com.budi.caquapplicaton.retrofit.RefreshTokenRequest
import com.budi.caquapplicaton.retrofit.SoftSkillDetail
import com.budi.caquapplicaton.utils.SharedPreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SoftskillViewModel(
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModel() {

    private val api = BaseClient.getSoftSkillsApi()

    private val _softSkills = MutableLiveData<List<String>>()
    val softSkills: LiveData<List<String>> get() = _softSkills

    private val _softSkillDetail = MutableLiveData<SoftSkillDetail>()
    val softSkillDetail: LiveData<SoftSkillDetail> get() = _softSkillDetail

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Fungsi untuk mendapatkan token baru dengan menggunakan refresh token yang ada
    private suspend fun getNewAccessToken(): Boolean {
        val refreshToken = sharedPreferencesHelper.getRefreshToken()
        if (refreshToken.isNullOrEmpty()) {
            _errorMessage.postValue("Refresh token not available. Please log in.")
            return false
        }

        return try {
            val response = withContext(Dispatchers.IO) {
                ApiClient.getClient().create(AuthService::class.java)
                    .refreshAuthToken(RefreshTokenRequest(refreshToken))
            }

            if (response.isSuccessful) {
                response.body()?.let {
                    val newAccessToken = it.accessToken
                    sharedPreferencesHelper.saveTokens(newAccessToken, refreshToken) // Simpan token baru
                    return true
                }
                false
            } else {
                _errorMessage.postValue("Failed to refresh token: ${response.message()}")
                false
            }
        } catch (e: Exception) {
            _errorMessage.postValue("Error refreshing token: ${e.message}")
            false
        }
    }

    // Mendapatkan data soft skills
    suspend fun fetchSoftSkills() {
        val token = sharedPreferencesHelper.getAccessToken()
        if (token.isNullOrEmpty()) {
            _errorMessage.postValue("Token not available. Please log in.")
            return
        }

        try {
            val response = withContext(Dispatchers.IO) {
                api.getSoftSkillNames("Bearer $token")
            }

            if (response.isSuccessful) {
                response.body()?.let {
                    _softSkills.postValue(it.data)
                }
            } else if (response.code() == 401) { // Token kadaluarsa
                if (getNewAccessToken()) {
                    fetchSoftSkills() // Retry dengan token baru
                }
            } else {
                _errorMessage.postValue("Failed to fetch soft skills: Session has Expired Please Relogin ${response.message()}")
            }
        } catch (e: Exception) {
            _errorMessage.postValue("Error: ${e.message}")
        }
    }

    // Mendapatkan detail soft skill berdasarkan nama
    suspend fun fetchSoftSkillDetail(name: String) {
        val token = sharedPreferencesHelper.getAccessToken()
        if (token.isNullOrEmpty()) {
            _errorMessage.postValue("Token not available. Please log in.")
            return
        }

        try {
            val encodedName = Uri.encode(name)
            val response = withContext(Dispatchers.IO) {
                api.getSoftSkillDetail(encodedName, "Bearer $token")
            }

            if (response.isSuccessful) {
                response.body()?.let {
                    _softSkillDetail.postValue(it)
                }
            } else if (response.code() == 401) { // Token kadaluarsa
                if (getNewAccessToken()) {
                    fetchSoftSkillDetail(name) // Retry dengan token baru
                }
            } else {
                _errorMessage.postValue("Failed to fetch soft skill detail: ${response.message()}")
            }
        } catch (e: Exception) {
            _errorMessage.postValue("Error: ${e.message}")
        }
    }
}
