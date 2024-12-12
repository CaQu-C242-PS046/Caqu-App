package com.budi.caquapplicaton.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.budi.caquapplication.R
import com.budi.caquapplication.databinding.FragmentHomeBinding
import com.budi.caquapplicaton.retrofit.QuizClient
import com.budi.caquapplicaton.ui.quiz.CareerRecommendationActivity
import com.budi.caquapplicaton.ui.quiz.QuizActivity
import com.budi.caquapplicaton.utils.SharedPreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        (activity as AppCompatActivity).supportActionBar?.hide()

        val username = sharedPreferencesHelper.getUsername() ?: "User"
        homeViewModel.setText(getString(R.string.halo, username))

        homeViewModel.text.observe(viewLifecycleOwner) { welcomeText ->
            binding.welcomeText.text = welcomeText
        }

        loadCareerHistory()

        binding.recommendButton.setOnClickListener {
            checkQuizStatusAndNavigate()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().moveTaskToBack(true)
                }
            }
        )

        return binding.root
    }

    private fun checkQuizStatusAndNavigate() {
        val token = "Bearer ${sharedPreferencesHelper.getAccessToken()}"

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = QuizClient.getQuizApi().getQuizStatus(token).execute()
                if (response.isSuccessful) {
                    val quizStatus = response.body()?.quizStatus

                    withContext(Dispatchers.Main) {
                        if (quizStatus.isNullOrEmpty()) {
                            sharedPreferencesHelper.saveQuizStatus(emptyList())
                            startActivity(Intent(requireContext(), QuizActivity::class.java))
                        } else {
                            sharedPreferencesHelper.saveQuizStatus(quizStatus)
                            val allAnswered = quizStatus.all { it.answered }

                            if (allAnswered) {
                                navigateToRecommendation()
                            } else {
                                startActivity(Intent(requireContext(), QuizActivity::class.java))
                            }
                        }
                    }
                } else {
                    showToast("Failed to load quiz status.")
                }
            } catch (e: Exception) {
                showToast("Error: ${e.message}")
            }
        }
    }

    private fun navigateToRecommendation() {
        val lastRecommendation = sharedPreferencesHelper.getLastRecommendation()
        val intent = Intent(requireContext(), CareerRecommendationActivity::class.java).apply {
            putExtra(CareerRecommendationActivity.EXTRA_RECOMMENDATION, lastRecommendation)
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadCareerHistory() {
        val token = "Bearer ${sharedPreferencesHelper.getAccessToken()}"

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Memanggil API untuk mendapatkan daftar karier
                val response = QuizClient.getQuizApi().getCareerName(token)
                if (response.isSuccessful) {
                    // Mengambil body respons dan memetakan ke daftar karier
                    val careerHistory = response.body()?.history.orEmpty()
                    val firstCareer = careerHistory.firstOrNull()?.recommendedCareer

                    withContext(Dispatchers.Main) {
                        if (firstCareer != null) {
                            // Menampilkan elemen pertama
                            binding.historyText.text = "- $firstCareer"
                        } else {
                            // Menampilkan teks jika data kosong
                            binding.historyText.text = getString(R.string.no_history_found)
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Failed to fetch career history. Error code: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Menampilkan pesan error jika ada pengecualian
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
