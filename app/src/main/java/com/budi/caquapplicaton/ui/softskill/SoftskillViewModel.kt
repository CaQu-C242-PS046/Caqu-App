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

        try {
            val response = withContext(Dispatchers.IO) {
                ApiClient.getClient().create(AuthService::class.java)
                    .refreshAuthToken(RefreshTokenRequest(refreshToken))
            }

            if (response.isSuccessful) {
                response.body()?.let {
                    // Menyimpan token baru
                    sharedPreferencesHelper.saveTokens(it.accessToken, refreshToken)
                }
                return true
            } else {
                _errorMessage.postValue("Failed to refresh token: ${response.message()}")
                return false
            }
        } catch (e: Exception) {
            _errorMessage.postValue("Error refreshing token: ${e.message}")
            return false
        }
    }

    suspend fun fetchSoftSkills() {
        var token = sharedPreferencesHelper.getAccessToken()
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
            } else if (response.code() == 401) { // Token expired
                if (getNewAccessToken()) {
                    token = sharedPreferencesHelper.getAccessToken() ?: return
                    val retryResponse = withContext(Dispatchers.IO) {
                        api.getSoftSkillNames("Bearer $token")
                    }

                    if (retryResponse.isSuccessful) {
                        retryResponse.body()?.let {
                            _softSkills.postValue(it.data)
                        }
                    } else {
                        _errorMessage.postValue("Failed to fetch soft skills after refreshing token.")
                    }
                }
            } else {
                _errorMessage.postValue("Failed to fetch soft skills: ${response.message()}")
            }
        } catch (e: Exception) {
            _errorMessage.postValue("Error: ${e.message}")
        }
    }

    suspend fun fetchSoftSkillDetail(name: String) {
        var token = sharedPreferencesHelper.getAccessToken()
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
            } else if (response.code() == 401) { // Token expired
                if (getNewAccessToken()) {
                    token = sharedPreferencesHelper.getAccessToken() ?: return
                    val retryResponse = withContext(Dispatchers.IO) {
                        api.getSoftSkillDetail(encodedName, "Bearer $token")
                    }

                    if (retryResponse.isSuccessful) {
                        retryResponse.body()?.let {
                            _softSkillDetail.postValue(it)
                        }
                    } else {
                        _errorMessage.postValue("Failed to fetch soft skill detail after refreshing token.")
                    }
                }
            } else {
                _errorMessage.postValue("Failed to fetch soft skill detail: ${response.message()}")
            }
        } catch (e: Exception) {
            _errorMessage.postValue("Error: ${e.message}")
        }
    }
}
