package com.budi.caquapplicaton.ui.career

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CareerViewModelFactory(private val repository: CareerRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CareerViewModel::class.java)) {
            return CareerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
