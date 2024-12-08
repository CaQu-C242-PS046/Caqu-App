package com.budi.caquapplicaton.ui.softskill


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.budi.caquapplication.utils.SharedPreferencesHelper

class SoftskillViewModelFactory(
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SoftskillViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SoftskillViewModel(sharedPreferencesHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
