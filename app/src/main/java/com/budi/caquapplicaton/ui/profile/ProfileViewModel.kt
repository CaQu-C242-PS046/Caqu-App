package com.budi.caquapplicaton.ui.profile

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.budi.caquapplication.utils.SharedPreferencesHelper


class ProfileViewModel : ViewModel() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _userStats = MutableLiveData<String>()
    val userStats: LiveData<String> = _userStats

    private val _shouldNavigateToChangePassword = MutableLiveData<Boolean>()
    val shouldNavigateToChangePassword: LiveData<Boolean> = _shouldNavigateToChangePassword

    private val _shouldNavigateToTermsCondition = MutableLiveData<Boolean>()
    val shouldNavigateToTermsCondition: LiveData<Boolean> = _shouldNavigateToTermsCondition

    private val _shouldLogout = MutableLiveData<Boolean>()
    val shouldLogout: LiveData<Boolean> = _shouldLogout

    fun initialize(context: Context) {
        sharedPreferencesHelper = SharedPreferencesHelper(context)

        val username = sharedPreferencesHelper.getUsername() ?: "User"
        _username.value = username

        val hasRecommendation = sharedPreferencesHelper.getAccessToken() != null
        _userStats.value = if (hasRecommendation) {
            "Your stats: Expert Level"
        } else {
            "Awaiting recommendations..."
        }
    }

    fun onChangePasswordClicked() {
        _shouldNavigateToChangePassword.value = true
    }

    fun onTermsClicked() {
        _shouldNavigateToTermsCondition.value = true
    }

    fun onLogOutClicked() {
        sharedPreferencesHelper.clearUserData()
        _shouldLogout.value = true
    }
}
