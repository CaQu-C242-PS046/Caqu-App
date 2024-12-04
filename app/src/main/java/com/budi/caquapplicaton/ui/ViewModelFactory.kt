package com.budi.caquapplicaton.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.budi.caquapplicaton.ui.change.ChangePasswordViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChangePasswordViewModel::class.java)) {
            return ChangePasswordViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}