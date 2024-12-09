package com.budi.caquapplicaton.ui.reset

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budi.caquapplication.R
import com.budi.caquapplicaton.retrofit.ApiClient
import com.budi.caquapplicaton.retrofit.AuthService
import com.budi.caquapplicaton.retrofit.ResetPasswordRequest
import com.budi.caquapplicaton.retrofit.ResetPasswordResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val backButton: ImageView = findViewById(R.id.backButton)
        val confirmButton: Button = findViewById(R.id.confirm_change_button)
        val emailEditText: EditText = findViewById(R.id.new_password)

        backButton.setOnClickListener {
            onBackPressed()
        }

        confirmButton.setOnClickListener {
            val email = emailEditText.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, getString(R.string.email_empty_warning), Toast.LENGTH_SHORT).show()
            } else {
                // Call the API to send the reset password email
                resetPasswordRequest(email)
            }
        }
    }

    private fun resetPasswordRequest(email: String) {
        // Initialize the Retrofit service
        val authService = ApiClient.getClient().create(AuthService::class.java)

        // Create the request object
        val resetPasswordRequest = ResetPasswordRequest(email)

        // Make the API call
        authService.forgotPassword(resetPasswordRequest).enqueue(object : Callback<ResetPasswordResponse> {
            override fun onResponse(
                call: Call<ResetPasswordResponse>,
                response: Response<ResetPasswordResponse>
            ) {
                if (response.isSuccessful) {
                    // Display success message
                    Toast.makeText(
                        this@ResetPassword,
                        getString(R.string.email_sent_success),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Display error message if email is not found
                    Toast.makeText(
                        this@ResetPassword,
                        getString(R.string.email_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                // Display network failure message
                Toast.makeText(
                    this@ResetPassword,
                    getString(R.string.network_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
