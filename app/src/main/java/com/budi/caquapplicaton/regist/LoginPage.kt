package com.budi.caquapplicaton.regist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budi.caquapplication.R
import com.budi.caquapplication.databinding.LoginPageBinding
import com.budi.caquapplicaton.MainActivity
import com.budi.caquapplicaton.retrofit.ApiClient
import com.budi.caquapplicaton.retrofit.AuthService
import com.budi.caquapplicaton.retrofit.LoginRequest
import com.budi.caquapplicaton.retrofit.LoginResponse
import com.budi.caquapplicaton.ui.reset.ResetPassword
import com.budi.caquapplicaton.utils.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPage : AppCompatActivity() {

    private lateinit var binding: LoginPageBinding
    private lateinit var apiService: AuthService
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        val retrofit = ApiClient.getClient()
        apiService = retrofit.create(AuthService::class.java)

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (validateInputs(username, password)) {
                login(username, password)
            }
        }

        // Fitur show/hide password
        binding.showHidePasswordIcon.setOnClickListener {
            togglePasswordVisibility()
        }

        binding.forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
        }

        binding.signupHereReg.setOnClickListener {
            val intent = Intent(this, RegistPage::class.java)
            startActivity(intent)
        }
    }

    private fun validateInputs(username: String, password: String): Boolean {
        return when {
            username.isEmpty() -> {
                binding.usernameEditText.error = "Username tidak boleh kosong"
                false
            }
            password.isEmpty() -> {
                binding.passwordEditText.error = "Kata sandi tidak boleh kosong"
                false
            }
            password.length < 8 -> {
                binding.passwordEditText.error = "Kata sandi harus memiliki minimal 8 karakter"
                false
            }
            else -> true
        }
    }

    private fun login(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)
        val call = apiService.login(loginRequest)

        binding.loginButton.isEnabled = false

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                binding.loginButton.isEnabled = true

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.success == true) {
                        sharedPreferencesHelper.saveTokens(
                            loginResponse.accessToken,
                            loginResponse.refreshToken
                        )
                        sharedPreferencesHelper.saveUsername(username)

                        val intent = Intent(this@LoginPage, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginPage,
                            "Login gagal: ${loginResponse?.message ?: "Kesalahan tidak diketahui"}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@LoginPage,
                        "Login gagal: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                binding.loginButton.isEnabled = true
                Toast.makeText(this@LoginPage, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            binding.passwordEditText.inputType =
                android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.showHidePasswordIcon.setImageResource(R.drawable.baseline_remove_red_eye_24)
        } else {
            binding.passwordEditText.inputType =
                android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.showHidePasswordIcon.setImageResource(R.drawable.baseline_visibility_off_24)
        }
        binding.passwordEditText.setSelection(binding.passwordEditText.text.length) // Retain cursor position
    }
}


