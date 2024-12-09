package com.budi.caquapplicaton.ui.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budi.caquapplicaton.retrofit.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizViewModel : ViewModel() {

    private val _questionResponse = MutableLiveData<QuestionResponse>()
    val questionResponse: LiveData<QuestionResponse> get() = _questionResponse

    private val _quizStatusResponse = MutableLiveData<QuizStatusResponse>()
    val quizStatusResponse: LiveData<QuizStatusResponse> get() = _quizStatusResponse

    private val _submitResponse = MutableLiveData<GenericResponse>()
    val submitResponse: LiveData<GenericResponse> get() = _submitResponse

    var totalQuestions: Int = 0

    fun loadQuestion(questionId: Int, token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = QuizClient.getQuizApi().getQuestion(questionId, token).execute()
                if (response.isSuccessful) {
                    response.body()?.let {
                        totalQuestions = it.questionNumber
                        withContext(Dispatchers.Main) {
                            _questionResponse.value = it
                        }
                    }
                }
            } catch (e: Exception) {
                // Handle exception if needed
            }
        }
    }

    fun submitAnswer(questionId: Int, answerValue: Int, token: String) {
        val request = AnswerRequest(question_id = questionId, answer = answerValue.toString())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = QuizClient.getQuizApi().submitAnswer(request, token).execute()
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        _submitResponse.value = response.body()
                    }
                }
            } catch (e: Exception) {
                // Handle exception if needed
            }
        }
    }

    fun checkQuizStatus(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = QuizClient.getQuizApi().getQuizStatus(token).execute()
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        _quizStatusResponse.value = response.body()
                    }
                }
            } catch (e: Exception) {
                // Handle exception if needed
            }
        }
    }
}
