package com.budi.caquapplicaton.ui.career

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budi.caquapplicaton.retrofit.CareerResponse
import kotlinx.coroutines.launch

class CareerViewModel(private val repository: CareerRepository) : ViewModel() {

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
                } else {
                    _errorMessage.postValue("Failed to fetch data: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("An error occurred: ${e.message}")
            }
        }
    }
}
