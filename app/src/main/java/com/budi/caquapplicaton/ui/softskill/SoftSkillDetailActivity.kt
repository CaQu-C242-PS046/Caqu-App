package com.budi.caquapplicaton.ui.softskill

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.budi.caquapplication.R
import com.budi.caquapplicaton.retrofit.SoftSkillDetail
import com.bumptech.glide.Glide

class SoftSkillDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soft_skill_detail)

        val tvSoftSkillName: TextView = findViewById(R.id.tvSoftSkillName)
        val ivVideoThumbnail: ImageView = findViewById(R.id.ivVideoThumbnail)
        val tvVideoTitle: TextView = findViewById(R.id.tvVideoTitle)
        val tvVideoDescription: TextView = findViewById(R.id.tvVideoDescription)

        // Ambil detail dari intent yang diterima
        val detail = intent.getParcelableExtra<SoftSkillDetail>("softSkillDetail")

        // Pastikan detail tidak null dan lakukan pengaturan tampilan
        detail?.let {
            tvSoftSkillName.text = it.nama_ss

            // Menampilkan thumbnail video
            Glide.with(this)
                .load(it.video.thumbnails.high.url)
                .into(ivVideoThumbnail)

            tvVideoTitle.text = it.video.title
            tvVideoDescription.text = it.video.description
        } ?: run {
            // Menangani jika detail null (misalnya jika data tidak diterima dengan benar)
            tvSoftSkillName.text = "Data tidak tersedia"
            tvVideoTitle.text = "Video tidak tersedia"
            tvVideoDescription.text = "Deskripsi tidak tersedia"
        }
    }
}
