package com.budi.caquapplicaton.ui.career

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.budi.caquapplicaton.room.AuthRepository

class CareerViewModelFactory(
    private val repository: CareerRepository,
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CareerViewModel::class.java)) {
            return CareerViewModel(repository, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

