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

        // Initialize UI elements
        val backButton: ImageView = findViewById(R.id.backButton)
        val confirmButton: Button = findViewById(R.id.confirm_change_button)
        val emailEditText: EditText = findViewById(R.id.emailEditText)  // Corrected ID

        // Back button to navigate to previous screen
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Set click listener for the confirm button to call the API
        confirmButton.setOnClickListener {
            val email = emailEditText.text.toString()

            // Validate email input
            if (email.isEmpty()) {
                Toast.makeText(this, getString(R.string.email_empty_warning), Toast.LENGTH_SHORT).show()
            } else {
                // Call the API to send the reset password email
                sendForgotPasswordRequest(email)
            }
        }
    }

    private fun sendForgotPasswordRequest(email: String) {
        // Initialize Retrofit service
        val authService = ApiClient.getClient().create(AuthService::class.java)

        // Create the ForgotPasswordRequest object
        val forgotPasswordRequest = ForgotPasswordRequest(email)

        // Make the API call
        authService.forgotPassword(forgotPasswordRequest).enqueue(object : Callback<ForgotPasswordResponse> {
            override fun onResponse(
                call: Call<ForgotPasswordResponse>,
                response: Response<ForgotPasswordResponse>
            ) {
                if (response.isSuccessful) {
                    // If email reset was successful, show success message and navigate to NewPasswordActivity
                    Toast.makeText(
                        this@ResetPasswordActivity,
                        getString(R.string.email_sent_success),
                        Toast.LENGTH_SHORT
                    ).show()

                    // Navigate to NewPasswordActivity
                    val intent = Intent(this@ResetPasswordActivity, NewPasswordActivity::class.java)
                    intent.putExtra("email", email) // Pass email to the next activity
                    startActivity(intent)
                    finish() // Optionally finish this activity so user cannot go back
                } else {
                    // If email is not found or an error occurs
                    Toast.makeText(
                        this@ResetPasswordActivity,
                        getString(R.string.email_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                // Display network failure message
                Toast.makeText(
                    this@ResetPasswordActivity,
                    getString(R.string.network_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
