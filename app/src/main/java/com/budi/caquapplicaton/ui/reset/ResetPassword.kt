package com.budi.caquapplicaton.ui.reset

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budi.caquapplication.R

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
                Toast.makeText(this, getString(R.string.email_sent_success), Toast.LENGTH_SHORT).show()
                // TODO: Tambahkan logika untuk mengirim permintaan reset password ke backend
            }
        }
    }
}
