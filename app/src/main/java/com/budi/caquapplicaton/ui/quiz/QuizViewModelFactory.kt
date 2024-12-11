package com.budi.caquapplicaton.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.budi.caquapplicaton.utils.SharedPreferencesHelper

class QuizViewModelFactory(
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            QuizViewModel(sharedPreferencesHelper) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
