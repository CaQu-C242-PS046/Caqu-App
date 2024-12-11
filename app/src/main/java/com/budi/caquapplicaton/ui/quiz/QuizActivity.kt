package com.budi.caquapplicaton.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.budi.caquapplication.R
import com.budi.caquapplication.databinding.ActivityQuizQuestionBinding
import com.budi.caquapplicaton.utils.SharedPreferencesHelper

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizQuestionBinding
    private val viewModel: QuizViewModel by viewModels { QuizViewModelFactory(SharedPreferencesHelper(this)) }
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private var token: String = ""
    private var currentQuestionId = 1
    private var selectedAnswerValue: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesHelper = SharedPreferencesHelper(this)
        val accessToken = sharedPreferencesHelper.getAccessToken()
        token = "Bearer $accessToken"

        setupObservers()
        setupAnswerButtons()

        viewModel.loadQuestion(currentQuestionId, token)

        binding.nextBtn.setOnClickListener {
            if (selectedAnswerValue != 0) {
                viewModel.submitAnswer(currentQuestionId, selectedAnswerValue, token)
            } else {
                Toast.makeText(this, "Pilih jawaban terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        viewModel.questionResponse.observe(this) { question ->
            binding.questionTextview.text = question.question
            binding.questionIndicatorTextview.text = "Question $currentQuestionId / ${viewModel.totalQuestions}"
            resetButtonColors()
        }

        viewModel.submitResponse.observe(this) { response ->
            if (response.success) {
                viewModel.checkQuizStatus(token)
            } else {
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.quizStatusResponse.observe(this) { status ->
            val nextQuestion = status.quizStatus.firstOrNull { !it.answered }
            if (nextQuestion != null) {
                currentQuestionId = nextQuestion.questionId
                viewModel.loadQuestion(currentQuestionId, token)
            } else {
                viewModel.submitQuiz(token) // Semua pertanyaan dijawab
            }
        }

        viewModel.recommendationResponse.observe(this) { recommendation ->
            if (recommendation != null) {
                // Simpan rekomendasi ke SharedPreferences
                sharedPreferencesHelper.saveLastRecommendation(recommendation.predicted_career)

                // Pindah ke Activity CareerRecommendation
                val intent = Intent(this, CareerRecommendationActivity::class.java).apply {
                    putExtra(CareerRecommendationActivity.EXTRA_RECOMMENDATION, recommendation.predicted_career)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Rekomendasi tidak tersedia.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupAnswerButtons() {
        val buttonToValueMap = mapOf(
            binding.btnHappy to 5,
            binding.btnSmile to 4,
            binding.btnNeutral to 3,
            binding.btnSad to 2,
            binding.btnAngry to 1
        )

        for ((button, value) in buttonToValueMap) {
            button.setOnClickListener {
                selectedAnswerValue = value
                resetButtonColors()
                button.setBackgroundColor(getColor(R.color.button_selected_color))
            }
        }
    }

    private fun resetButtonColors() {
        val buttons = listOf(
            binding.btnHappy,
            binding.btnSmile,
            binding.btnNeutral,
            binding.btnSad,
            binding.btnAngry
        )
        for (button in buttons) {
            button.setBackgroundColor(getColor(R.color.button_default_color))
        }
    }
}
