package com.budi.caquapplicaton.ui.reset

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budi.caquapplication.R
import com.budi.caquapplicaton.regist.LoginPage
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
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)

        authService = RetrofitClient.getInstance().create(AuthService::class.java)

        // Inisialisasi views
        backButton = findViewById(R.id.backButton)
        emailEditText = findViewById(R.id.emailEditText)
        newPasswordEditText = findViewById(R.id.new_password)
        confirmChangeButton = findViewById(R.id.confirm_change_button)

        // Mengambil data dari intent
        resetToken = intent.getStringExtra("reset_token")
        email = intent.getStringExtra("email")

        // Pre-fill email jika tersedia
        email?.let {
            emailEditText.setText(it)
            emailEditText.isEnabled = false // Optional: mencegah user mengubah email
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

        confirmChangeButton.setOnClickListener {
            handlePasswordReset()
        }
    }

    private fun handlePasswordReset() {
        val email = emailEditText.text.toString()
        val newPassword = newPasswordEditText.text.toString()

        // Validasi input
        if (email.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this, "Email dan password baru harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (resetToken.isNullOrEmpty()) {
            Toast.makeText(this, "Token reset password tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        // Validasi password
        if (newPassword.length < 8) {
            Toast.makeText(this, "Password harus minimal 8 karakter", Toast.LENGTH_SHORT).show()
            return
        }

        val request = ResetPasswordRequest(email, newPassword)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = authService.resetPassword(resetToken!!, request)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@NewPasswordActivity, "Password berhasil diubah", Toast.LENGTH_SHORT).show()
                        // Redirect ke halaman login
                        val intent = Intent(this@NewPasswordActivity, LoginPage::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Gagal mengubah password"
                        Toast.makeText(this@NewPasswordActivity, errorMessage, Toast.LENGTH_SHORT).show()
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