package com.budi.caquapplicaton.ui.softskill

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.budi.caquapplication.utils.SharedPreferencesHelper
import com.budi.caquapplicaton.retrofit.BaseClient
import com.budi.caquapplicaton.retrofit.SoftSkillDetail
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
            } else {
                _errorMessage.postValue("Failed to fetch soft skills: ${response.message()}")
            }
        } catch (e: Exception) {
            _errorMessage.postValue("Error: ${e.message}")
        }
    }

    suspend fun fetchSoftSkillDetail(name: String) {
        val token = sharedPreferencesHelper.getAccessToken()
        if (token.isNullOrEmpty()) {
            _errorMessage.postValue("Token not available. Please log in.")
            return
        }

        try {
            // Encode the name parameter before making the API call
            val encodedName = Uri.encode(name)

            val response = withContext(Dispatchers.IO) {
                api.getSoftSkillDetail(encodedName, "Bearer $token")
            }
            if (response.isSuccessful) {
                response.body()?.let {
                    _softSkillDetail.postValue(it)
                }
            } else {
                _errorMessage.postValue("Failed to fetch soft skill detail: ${response.message()}")
            }
        } catch (e: Exception) {
            _errorMessage.postValue("Error: ${e.message}")
        }
    }

}
