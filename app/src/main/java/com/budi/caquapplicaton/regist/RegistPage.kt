package com.budi.caquapplicaton.regist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budi.caquapplication.R
import com.budi.caquapplication.databinding.RegisterPageBinding
import com.budi.caquapplicaton.MainActivity
import com.budi.caquapplicaton.retrofit.ApiClient
import com.budi.caquapplicaton.retrofit.AuthService
import com.budi.caquapplicaton.retrofit.RegisterRequest
import com.budi.caquapplicaton.retrofit.RegisterResponse
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistPage : AppCompatActivity() {

    private lateinit var binding: RegisterPageBinding
    private lateinit var apiService: AuthService
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private val TAG = "RegistPage"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Retrofit API Service
        val retrofit = ApiClient.getClient()
        apiService = retrofit.create(AuthService::class.java)

        // Inisialisasi Google Sign-In
        setupGoogleSignIn()

        // Setup click listeners
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.signupButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (validateInputs(username, email, password)) {
                register(username, email, password)
            }
        }

        binding.loginTextView.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
            finish()
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun validateInputs(username: String, email: String, password: String): Boolean {
        var isValid = true

        if (username.isEmpty() || username.length < 3) {
            binding.usernameEditText.error = "Username must be at least 3 characters"
            isValid = false
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditText.error = "Invalid email format"
            isValid = false
        }

        if (password.isEmpty() || password.length < 8) {
            binding.passwordEditText.error = "Password must be at least 8 characters"
            isValid = false
        }

        return isValid
    }

    private fun register(username: String, email: String, password: String) {
        binding.signupButton.isEnabled = false

        val registerRequest = RegisterRequest(username, email, password)
        apiService.register(registerRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                binding.signupButton.isEnabled = true

                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse?.success == true) {
                        Toast.makeText(this@RegistPage, registerResponse.message, Toast.LENGTH_SHORT).show()
                        navigateToMainActivity()
                    } else {
                        Toast.makeText(this@RegistPage, registerResponse?.message ?: "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@RegistPage, "Server error occurred", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                binding.signupButton.isEnabled = true
                Toast.makeText(this@RegistPage, "Failed to connect to server: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Network error", t)
            }
        })
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun setupGoogleSignIn() {
        oneTapClient = Identity.getSignInClient(this)

        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id)) // Pastikan ini sudah benar
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        binding.googleSignInButton.setOnClickListener {
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener { result ->
                    try {
                        startIntentSenderForResult(
                            result.pendingIntent.intentSender,
                            1001, null, 0, 0, 0
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Error starting sign-in intent: ${e.message}")
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken
                val displayName = credential.displayName

                if (idToken != null) {
                    Toast.makeText(this, "Google Sign-In Success! Welcome $displayName", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                } else {
                    Toast.makeText(this, "Google Sign-In Failed: Token is null", Toast.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                Log.e(TAG, "Google Sign-In failed: ${e.message}")
            }
        }
    }
}
