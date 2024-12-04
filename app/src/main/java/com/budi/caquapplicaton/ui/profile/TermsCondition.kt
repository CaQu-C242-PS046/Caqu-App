package com.budi.caquapplicaton.ui.profile

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.budi.caquapplication.R

class TermsCondition: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.terms_and_condition) // Pastikan nama file XML sesuai

        // Inisialisasi back button
        val backButton = findViewById<ImageView>(R.id.backButton)

        // Tambahkan logika untuk tombol kembali
        backButton.setOnClickListener {
            onBackPressed() // Menutup Activity ini dan kembali ke sebelumnya
        }
    }
}
