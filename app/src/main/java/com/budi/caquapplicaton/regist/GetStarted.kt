package com.budi.caquapplicaton.regist

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.budi.caquapplication.R

class GetStarted : AppCompatActivity() {

    private lateinit var registerButton: Button
    private lateinit var loginButton: Button
    private lateinit var animatedImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.get_started)


        registerButton = findViewById(R.id.button1)
        loginButton = findViewById(R.id.button2)
        animatedImage = findViewById(R.id.get_started)


        registerButton.setOnClickListener {
            navigateToRegister()
        }

        loginButton.setOnClickListener {
            navigateToLogin()
        }


        startImageAnimation()
    }

    private fun navigateToRegister() {
        try {
            val intent = Intent(this, RegistPage::class.java)
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("GetStarted", "Error navigating to register: ${e.message}")
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginPage::class.java)
        startActivity(intent)
    }

    private fun startImageAnimation() {
        val animator = ObjectAnimator.ofFloat(animatedImage, "translationY", 0f, 50f)
        animator.duration = 2000
        animator.repeatMode = ValueAnimator.REVERSE
        animator.repeatCount = ValueAnimator.INFINITE
        animator.start()
    }
}
