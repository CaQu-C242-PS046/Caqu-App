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
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmChangeButton: Button
    private lateinit var authService: AuthService
    private var resetToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)

        authService = RetrofitClient.getInstance().create(AuthService::class.java)

        backButton = findViewById(R.id.backButton)
        emailEditText = findViewById(R.id.emailEditText)
        newPasswordEditText = findViewById(R.id.new_password)
        confirmChangeButton = findViewById(R.id.confirm_change_button)

        resetToken = intent.getStringExtra("token")

        backButton.setOnClickListener {
            onBackPressed()
        }

        confirmChangeButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()
            if (email.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Email dan password baru harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (resetToken == null) {
                Toast.makeText(this, "Reset token tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val request = ResetPasswordRequest(email, newPassword)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = authService.resetPassword(resetToken!!, request)

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@NewPasswordActivity, "Password berhasil diubah", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@NewPasswordActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@NewPasswordActivity, "Gagal mengubah password: ${response.message()}", Toast.LENGTH_SHORT).show()
                            val errorMessage = response.errorBody()?.string()
                            Toast.makeText(this@NewPasswordActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@NewPasswordActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}