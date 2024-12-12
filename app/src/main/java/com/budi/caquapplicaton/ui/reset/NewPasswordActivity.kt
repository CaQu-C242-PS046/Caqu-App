package com.budi.caquapplicaton.ui.reset

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budi.caquapplication.R
import com.budi.caquapplicaton.MainActivity
import com.budi.caquapplicaton.retrofit.AuthService
import com.budi.caquapplicaton.retrofit.ResetPasswordRequest
import com.budi.caquapplicaton.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPasswordActivity : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var emailEditText: EditText
    private lateinit var resetCodeEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmChangeButton: Button
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)

        // Initialize Retrofit and the AuthService
        authService = RetrofitClient.getInstance().create(AuthService::class.java)

        // Find views by ID
        backButton = findViewById(R.id.backButton)
        emailEditText = findViewById(R.id.email)
        resetCodeEditText = findViewById(R.id.resetCode)
        newPasswordEditText = findViewById(R.id.new_password)
        confirmChangeButton = findViewById(R.id.confirm_change_button)

        // Back button listener
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Confirm change button listener
        confirmChangeButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val resetCode = resetCodeEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()

            // Validation: check if any fields are empty
            if (email.isEmpty() || resetCode.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Prepare the request data
            val request = ResetPasswordRequest(
                email = email,
                resetCode = resetCode,  // This would be the OTP or reset code entered by the user
                newPassword = newPassword
            )

            // Send the request to reset the password
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Call the API endpoint to reset the password
                    val response = authService.resetPassword(request)

                    // Handle the response
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@NewPasswordActivity, "Password successfully updated", Toast.LENGTH_SHORT).show()
                            // Redirect to the main activity
                            val intent = Intent(this@NewPasswordActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Handle failure
                            Toast.makeText(this@NewPasswordActivity, "Failed to change password: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    // Handle any errors
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@NewPasswordActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
