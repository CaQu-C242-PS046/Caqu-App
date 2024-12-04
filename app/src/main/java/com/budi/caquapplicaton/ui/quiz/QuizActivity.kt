package com.budi.caquapplicaton.ui.quiz

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budi.caquapplication.databinding.ActivityQuizQuestionBinding
import com.budi.caquapplicaton.retrofit.AnswerRequest
import com.budi.caquapplicaton.retrofit.GenericResponse
import com.budi.caquapplicaton.retrofit.QuestionResponse
import com.budi.caquapplicaton.retrofit.QuizClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizQuestionBinding
    private var currentQuestionId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadQuestion(currentQuestionId)

        binding.btnHappy.setOnClickListener { submitAnswer(currentQuestionId, "Happy") }
        binding.btnSmile.setOnClickListener { submitAnswer(currentQuestionId, "Smile") }
        binding.btnNeutral.setOnClickListener { submitAnswer(currentQuestionId, "Neutral") }
        binding.btnSad.setOnClickListener { submitAnswer(currentQuestionId, "Sad") }
        binding.btnAngry.setOnClickListener { submitAnswer(currentQuestionId, "Angry") }

        binding.nextBtn.setOnClickListener {
            // Navigate to next question
            currentQuestionId++
            loadQuestion(currentQuestionId)
        }
    }

    private fun loadQuestion(questionNumber: Int) {
        QuizClient.instance.getQuestion(questionNumber).enqueue(object : Callback<QuestionResponse> {
            override fun onResponse(call: Call<QuestionResponse>, response: Response<QuestionResponse>) {
                if (response.isSuccessful) {
                    val question = response.body()
                    binding.questionTextview.text = question?.question
                    binding.questionIndicatorTextview.text = "Question $questionNumber/20"
                } else {
                    Toast.makeText(this@QuizActivity, "Failed to load question", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
                Log.e("QuizActivity", "Error: ${t.message}")
                Toast.makeText(this@QuizActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun submitAnswer(questionId: Int, answer: String) {
        val answerRequest = AnswerRequest(question_id = questionId, answer = answer)
        QuizClient.instance.submitAnswer(answerRequest).enqueue(object : Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@QuizActivity, "Answer submitted: $answer", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@QuizActivity, "Failed to submit answer", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                Log.e("QuizActivity", "Error: ${t.message}")
                Toast.makeText(this@QuizActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
