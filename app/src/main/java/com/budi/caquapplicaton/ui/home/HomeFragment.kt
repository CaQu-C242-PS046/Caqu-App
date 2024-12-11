package com.budi.caquapplicaton.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.budi.caquapplication.R
import com.budi.caquapplication.databinding.FragmentHomeBinding
import com.budi.caquapplicaton.retrofit.QuizClient
import com.budi.caquapplicaton.ui.quiz.QuizActivity
import com.budi.caquapplicaton.ui.quiz.CareerRecommendationActivity
import com.budi.caquapplicaton.utils.SharedPreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var homeViewModel: HomeViewModel  // Deklarasi HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Initialize ViewModel dan SharedPreferencesHelper
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())

        (activity as AppCompatActivity).supportActionBar?.hide()

        // Menyimpan dan menampilkan nama pengguna menggunakan ViewModel
        val username = sharedPreferencesHelper.getUsername() ?: "User"
        homeViewModel.setText(getString(R.string.halo, username))

        // Observasi perubahan teks yang ada di ViewModel
        homeViewModel.text.observe(viewLifecycleOwner, { welcomeText ->
            binding.welcomeText.text = welcomeText
        })

        binding.recommendButton.setOnClickListener {
            checkQuizStatusAndNavigate()
        }

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
                        if (quizStatus == null || quizStatus.isEmpty()) {
                            // Jika belum pernah mengerjakan kuis
                            sharedPreferencesHelper.saveQuizStatus(emptyList())
                            startActivity(Intent(requireContext(), QuizActivity::class.java))
                        } else {
                            // Simpan status kuis
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
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Gagal memuat status kuis.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Terjadi kesalahan: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
