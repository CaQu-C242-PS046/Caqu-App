package com.budi.caquapplicaton.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    fun setText(value: String) {
        _text.value = value
    }
}
