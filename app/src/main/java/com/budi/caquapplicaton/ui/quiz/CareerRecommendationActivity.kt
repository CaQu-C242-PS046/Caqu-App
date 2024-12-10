package com.budi.caquapplicaton.ui.quiz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.budi.caquapplication.databinding.ActivityCareerRecommendationBinding
import com.budi.caquapplicaton.MainActivity
import com.budi.caquapplicaton.room.AppDatabase
import com.budi.caquapplicaton.room.CareerRecommendationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CareerRecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCareerRecommendationBinding
    private lateinit var database: AppDatabase

    companion object {
        const val EXTRA_RECOMMENDATION = "extra_recommendation"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCareerRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi database
        database = AppDatabase.getInstance(this)

        // Ambil data rekomendasi dari intent
        val recommendation = intent.getStringExtra(EXTRA_RECOMMENDATION) ?: "Rekomendasi tidak tersedia"
        binding.recommendationTextView.text = recommendation

        // Simpan rekomendasi ke Room Database
        saveRecommendationToDatabase(recommendation)

        binding.nextButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun saveRecommendationToDatabase(recommendation: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val recommendationEntity = CareerRecommendationEntity(recommendation = recommendation)
            database.careerRecommendationDao().insertRecommendation(recommendationEntity)
        }
    }
}
