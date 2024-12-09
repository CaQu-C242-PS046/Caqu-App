package com.budi.caquapplicaton.ui.softskill

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.budi.caquapplication.R
import com.budi.caquapplicaton.MainActivity
import com.budi.caquapplicaton.retrofit.SoftSkillDetail
import com.bumptech.glide.Glide

class SoftSkillDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soft_skill_detail)

        // Toolbar setup
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val backButton: ImageView = findViewById(R.id.backButton)
        val toolbarTitle: TextView = findViewById(R.id.toolbarTitle)

        // Set toolbar title
        toolbarTitle.text = "Soft Skill"

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java) // Replace `MainActivity` with your hosting activity class
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish() // Ensure the current activity is closed
        }


        // UI elements
        val tvSoftSkillName: TextView = findViewById(R.id.tvSoftSkillName)
        val ivVideoThumbnail: ImageView = findViewById(R.id.ivVideoThumbnail)
        val tvVideoTitle: TextView = findViewById(R.id.tvVideoTitle)
        val tvVideoDescription: TextView = findViewById(R.id.tvVideoDescription)
        val tvVideoLink: TextView = findViewById(R.id.tvVideoLink)

        // Get the SoftSkillDetail object passed via Intent
        val detail = intent.getParcelableExtra<SoftSkillDetail>("softSkillDetail")

        // Populate UI elements with the data
        detail?.let {
            tvSoftSkillName.text = it.nama_ss

            // Load video thumbnail using Glide
            Glide.with(this)
                .load("https://img.youtube.com/vi/${getYouTubeVideoId(it.video.videoLink)}/hqdefault.jpg") // YouTube thumbnail URL
                .into(ivVideoThumbnail)

            tvVideoTitle.text = it.video.title
            tvVideoDescription.text = it.video.description

            // Set the video link text and make it clickable
            tvVideoLink.text = "Watch Video"
            tvVideoLink.setOnClickListener {
                // Open video link in a web browser
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(detail.video.videoLink) // Use videoLink from JSON
                startActivity(intent)
            }
        } ?: run {
            // Handle null data (e.g., no intent data passed)
            tvSoftSkillName.text = "Data not available"
            tvVideoTitle.text = "Video not available"
            tvVideoDescription.text = "Description not available"
            tvVideoLink.text = "Link not available"
        }
    }

    // Helper function to extract YouTube video ID from the video link
    private fun getYouTubeVideoId(videoLink: String): String {
        return Uri.parse(videoLink).getQueryParameter("v") ?: ""
    }
}