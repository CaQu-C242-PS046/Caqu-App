package com.budi.caquapplicaton.ui.career

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CareerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is career Fragment"
    }
    val text: LiveData<String> = _text
}