package com.budi.caquapplicaton.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.budi.caquapplication.R
import com.budi.caquapplication.utils.SharedPreferencesHelper
import com.budi.caquapplicaton.MainActivity
import com.budi.caquapplicaton.regist.GetStarted

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val countDownTimer = object : CountDownTimer(3000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Tidak ada yang perlu dilakukan pada setiap tick
            }

            override fun onFinish() {
                navigateToNextScreen()
            }
        }
        countDownTimer.start()
    }

    private fun navigateToNextScreen() {

        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        val nextActivity = if (sharedPreferencesHelper.isLoggedIn()) {
            MainActivity::class.java
        } else {
            GetStarted::class.java
        }

        val intent = Intent(this, nextActivity)
        startActivity(intent)
        finish()
    }
}