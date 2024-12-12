package com.budi.caquapplicaton.ui.change

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.budi.caquapplication.R
import com.budi.caquapplicaton.ui.ViewModelFactory
import com.budi.caquapplicaton.utils.SharedPreferencesHelper

class ChangePassword : AppCompatActivity() {

    private lateinit var oldPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var showHideOldPasswordIcon: ImageView
    private lateinit var showHideNewPasswordIcon: ImageView
    private lateinit var confirmChangeButton: Button
    private lateinit var backButton: ImageView

    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        // Inisialisasi SharedPreferencesHelper dan ViewModel
        sharedPreferencesHelper = SharedPreferencesHelper(this)
        changePasswordViewModel = ViewModelProvider(this, ViewModelFactory(applicationContext)).get(ChangePasswordViewModel::class.java)

        oldPasswordEditText = findViewById(R.id.old_password)
        newPasswordEditText = findViewById(R.id.new_password)
        showHideOldPasswordIcon = findViewById(R.id.showHidePasswordIcon)
        showHideNewPasswordIcon = findViewById(R.id.showHidePasswordIcon2)
        confirmChangeButton = findViewById(R.id.confirm_change_button)
        backButton = findViewById(R.id.backButton)

        setupPasswordVisibilityToggle(oldPasswordEditText, showHideOldPasswordIcon)
        setupPasswordVisibilityToggle(newPasswordEditText, showHideNewPasswordIcon)

        setupPasswordValidation()

        // Menangani klik tombol "Kembali"
        backButton.setOnClickListener { onBackPressed() }

        // Menangani klik tombol "Confirm Change"
        confirmChangeButton.setOnClickListener {
            val oldPassword = oldPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()
            changePasswordViewModel.changePassword(oldPassword, newPassword)
        }

        // Observasi perubahan status loading
        changePasswordViewModel.isLoading.observe(this, Observer { isLoading ->
            // Tampilkan loading jika status loading true
            confirmChangeButton.isEnabled = !isLoading
        })

        // Observasi pesan respons dari ViewModel
        changePasswordViewModel.responseMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setupPasswordVisibilityToggle(editText: EditText, iconView: ImageView) {
        var isPasswordVisible = false
        iconView.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            val inputType = if (isPasswordVisible) {
                android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            editText.inputType = inputType
            editText.typeface = editText.typeface // Preserve cursor position
            editText.setSelection(editText.text.length)

            val visibilityIcon = if (isPasswordVisible) {
                R.drawable.baseline_remove_red_eye_24
            } else {
                R.drawable.baseline_visibility_off_24
            }
            iconView.setImageDrawable(ContextCompat.getDrawable(this, visibilityIcon))
        }
    }

    private fun setupPasswordValidation() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePasswords()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        oldPasswordEditText.addTextChangedListener(textWatcher)
        newPasswordEditText.addTextChangedListener(textWatcher)
    }

    private fun validatePasswords() {
        val oldPassword = oldPasswordEditText.text.toString()
        val newPassword = newPasswordEditText.text.toString()

        val isOldPasswordValid = oldPassword.isNotBlank()
        val isNewPasswordValid = isValidPassword(newPassword)

        confirmChangeButton.isEnabled = isOldPasswordValid && isNewPasswordValid
    }

    private fun isValidPassword(password: String): Boolean {
        // Basic password validation
        return password.length >= 8 &&
                password.any { it.isUpperCase() } &&
                password.any { it.isLowerCase() } &&
                password.any { it.isDigit() }
    }
}
