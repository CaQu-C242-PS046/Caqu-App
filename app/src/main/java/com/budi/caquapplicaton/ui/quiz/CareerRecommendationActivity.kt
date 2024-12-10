package com.budi.caquapplicaton.ui.quiz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.budi.caquapplication.databinding.ActivityCareerRecommendationBinding
import com.budi.caquapplicaton.MainActivity

class CareerRecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCareerRecommendationBinding

    companion object {
        const val EXTRA_RECOMMENDATION = "extra_recommendation"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCareerRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recommendation = intent.getStringExtra(EXTRA_RECOMMENDATION) ?: "Rekomendasi tidak tersedia"
        binding.recommendationTextView.text = recommendation

        binding.nextButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
