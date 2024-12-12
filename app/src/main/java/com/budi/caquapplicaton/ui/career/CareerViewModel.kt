package com.budi.caquapplicaton.ui.career

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budi.caquapplicaton.retrofit.CareerResponse
import com.budi.caquapplicaton.room.AuthRepository
import kotlinx.coroutines.launch

class CareerViewModel(private val repository: CareerRepository, private val authRepository: AuthRepository) : ViewModel() {

    private val _careerDetails = MutableLiveData<CareerResponse>()
    val careerDetails: LiveData<CareerResponse> get() = _careerDetails

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchCareerDetails(name: String, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getCareerDetails(name, token)
                if (response.isSuccessful) {
                    _careerDetails.postValue(response.body())
                } else if (response.code() == 401) { // Token kadaluarsa
                    handleTokenExpiration(name)
                } else {
                    _errorMessage.postValue("Failed to fetch data: Please Relogin ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("An error occurred: ${e.message}")
            }
        }
    }

    private fun handleTokenExpiration(name: String) {
        viewModelScope.launch {
            try {
                val newToken = authRepository.refreshAccessToken()
                if (newToken != null) {
                    // Retry request dengan token baru
                    fetchCareerDetails(name, "Bearer $newToken")
                } else {
                    _errorMessage.postValue("Failed to refresh token. Please log in again.")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error refreshing token: ${e.message}")
            }
        }
    }
}
