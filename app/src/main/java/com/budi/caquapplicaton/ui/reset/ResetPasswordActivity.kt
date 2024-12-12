package com.budi.caquapplicaton.ui.reset

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budi.caquapplication.R
import com.budi.caquapplicaton.retrofit.ApiClient
import com.budi.caquapplicaton.retrofit.AuthService
import com.budi.caquapplicaton.retrofit.ForgotPasswordRequest
import com.budi.caquapplicaton.retrofit.ForgotPasswordResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val backButton: ImageView = findViewById(R.id.backButton)
        val confirmButton: Button = findViewById(R.id.confirm_change_button)
        val lanjutButton: Button = findViewById(R.id.lanjut) // "Lanjut" button
        val emailEditText: EditText = findViewById(R.id.emailEditText)

        // Back button listener
        backButton.setOnClickListener {
            onBackPressed()
        }

        // "Confirm" button listener to send password reset email
        confirmButton.setOnClickListener {
            val email = emailEditText.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, getString(R.string.email_empty_warning), Toast.LENGTH_SHORT).show()
            } else {
                sendForgotPasswordRequest(email)
            }
        }

        // "Lanjut" button listener to go to NewPasswordActivity without sending email
        lanjutButton.setOnClickListener {
            val email = emailEditText.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, getString(R.string.email_empty_warning), Toast.LENGTH_SHORT).show()
            } else {
                // Navigate to NewPasswordActivity without sending email
                val intent = Intent(this@ResetPasswordActivity, NewPasswordActivity::class.java)
                startActivity(intent)  // No email passed to NewPasswordActivity
            }
        }
    }

    // Function to send the Forgot Password request
    private fun sendForgotPasswordRequest(email: String) {
        val authService = ApiClient.getClient().create(AuthService::class.java)
        val forgotPasswordRequest = ForgotPasswordRequest(email)

        // Send the request to the server
        authService.forgotPassword(forgotPasswordRequest).enqueue(object : Callback<ForgotPasswordResponse> {
            override fun onResponse(
                call: Call<ForgotPasswordResponse>,
                response: Response<ForgotPasswordResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@ResetPasswordActivity,
                        getString(R.string.email_sent_success),
                        Toast.LENGTH_SHORT
                    ).show()

                    // Navigate to NewPasswordActivity after sending email
                    val intent = Intent(this@ResetPasswordActivity, NewPasswordActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@ResetPasswordActivity,
                        getString(R.string.email_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                Toast.makeText(
                    this@ResetPasswordActivity,
                    getString(R.string.network_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
