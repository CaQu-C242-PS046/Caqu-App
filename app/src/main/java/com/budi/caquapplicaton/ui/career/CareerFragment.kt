package com.budi.caquapplicaton.ui.career

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.budi.caquapplication.databinding.FragmentCareerBinding
import com.budi.caquapplicaton.retrofit.AuthService
import com.budi.caquapplicaton.retrofit.RetrofitClient
import com.budi.caquapplicaton.room.AuthRepository
import com.budi.caquapplicaton.utils.SharedPreferencesHelper
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class CareerFragment : Fragment() {

    private val TAG = "CareerFragment"

    private var _binding: FragmentCareerBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var authRepository: AuthRepository
    private val apiService = RetrofitClient.createService(AuthService::class.java)
    private lateinit var repository: CareerRepository

    private val viewModel: CareerViewModel by viewModels {
        CareerViewModelFactory(repository, authRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCareerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: Initializing components")

        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        authRepository = AuthRepository(apiService, sharedPreferencesHelper)
        repository = CareerRepository(apiService)

        val token = sharedPreferencesHelper.getAccessToken()
        Log.d(TAG, "onViewCreated: Token retrieved: $token")

        if (token.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Token tidak ditemukan, silakan login ulang", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "onViewCreated: Token not found")
            return
        }

        val careerName = sharedPreferencesHelper.getLastRecommendation()
        Log.d(TAG, "onViewCreated: Last recommendation retrieved: $careerName")

        if (careerName.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Rekomendasi tidak tersedia, silakan lakukan quiz", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "onViewCreated: Career recommendation not available")
            return
        }

        fetchCareerDetailsWithRetry(careerName, token)

    }

    private fun fetchCareerDetailsWithRetry(careerName: String, token: String) {
        Log.d(TAG, "fetchCareerDetailsWithRetry: Fetching career details for $careerName")
        viewModel.fetchCareerDetails(careerName, "Bearer $token")

        viewModel.careerDetails.observe(viewLifecycleOwner) { careerResponse ->
            Log.d(TAG, "fetchCareerDetailsWithRetry: Career details retrieved: $careerResponse")

            binding.careerName.text = careerResponse.namaKarir
            binding.descriptionText.text = careerResponse.insight.joinToString("\n")
            binding.skillsText.text = careerResponse.skill.joinToString("\n")
            binding.educationText.text = careerResponse.pendidikan.joinToString("\n")
            binding.playlistTitle.text = careerResponse.video.joinToString(", ") { it.snippet.title }
            binding.feedbackTitle.text = careerResponse.feedback.title


            Glide.with(this)
                .load(careerResponse.image)
                .into(binding.careerImage)

            binding.tvPlaylistLink.setOnClickListener {
                val playlistUrl = careerResponse.video.firstOrNull()?.snippet?.playlistLink ?: ""
                if (playlistUrl.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(playlistUrl)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "URL Playlist tidak tersedia", Toast.LENGTH_SHORT).show()
                }
            }

            binding.tvFeedbackLink.setOnClickListener {
                val feedbackUrl = careerResponse.feedback.videoLink
                if (feedbackUrl.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(feedbackUrl)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "URL Feedback tidak tersedia", Toast.LENGTH_SHORT).show()
                }
            }
        }


        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Log.e(TAG, "fetchCareerDetailsWithRetry: Error occurred: $errorMessage")
            if (errorMessage.contains("401")) {
                retryWithNewToken(careerName)
            } else {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun retryWithNewToken(careerName: String) {
        Log.d(TAG, "retryWithNewToken: Attempting to refresh token")
        lifecycleScope.launch {
            val newToken = authRepository.refreshAccessToken()
            if (newToken != null) {
                Log.d(TAG, "retryWithNewToken: New token retrieved: $newToken")
                fetchCareerDetailsWithRetry(careerName, newToken)
            } else {
                Toast.makeText(requireContext(), "Gagal memperbarui token, silakan login ulang", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "retryWithNewToken: Failed to refresh token")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

