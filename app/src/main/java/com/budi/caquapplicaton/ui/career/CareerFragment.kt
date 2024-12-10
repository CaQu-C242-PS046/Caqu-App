package com.budi.caquapplicaton.ui.career

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.fragment.app.viewModels
import com.budi.caquapplication.databinding.FragmentCareerBinding
import com.budi.caquapplicaton.retrofit.AuthService
import com.budi.caquapplicaton.retrofit.BaseService
import com.budi.caquapplicaton.retrofit.RetrofitClient
import com.budi.caquapplicaton.room.AuthRepository
import com.budi.caquapplicaton.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch

class CareerFragment : Fragment() {

    private var _binding: FragmentCareerBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private val apiService = RetrofitClient.createService(AuthService::class.java)
    private val repository = CareerRepository(apiService)
    private val viewModel: CareerViewModel by viewModels { CareerViewModelFactory(repository) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCareerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi SharedPreferencesHelper dan AuthRepository
        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        val authRepository = AuthRepository(apiService, sharedPreferencesHelper)

        // Ambil token dari SharedPreferences
        var token = sharedPreferencesHelper.getAccessToken()

        if (token.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Token tidak ditemukan, silakan login ulang", Toast.LENGTH_SHORT).show()
            return
        }

        // Ambil rekomendasi terakhir dari SharedPreferences
        val careerName = sharedPreferencesHelper.getLastRecommendation() ?: ""

        if (careerName.isEmpty()) {
            Toast.makeText(requireContext(), "Rekomendasi tidak tersedia, silakan lakukan quiz", Toast.LENGTH_SHORT).show()
            return
        }

        // Fetch data karir dengan token
        fetchCareerDetailsWithRetry(careerName, token, authRepository)
    }

    private fun fetchCareerDetailsWithRetry(careerName: String, token: String, authRepository: AuthRepository) {
        viewModel.fetchCareerDetails(careerName, "Bearer $token")

        viewModel.careerDetails.observe(viewLifecycleOwner) { careerResponse ->
            // Update UI dengan data dari API
            binding.careerName.text = careerResponse.namaKarir
            binding.descriptionText.text = careerResponse.insight.joinToString("\n")
            binding.skillsText.text = careerResponse.skill.joinToString("\n")
            binding.educationText.text = careerResponse.pendidikan.joinToString("\n")
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage.contains("401")) { // Jika token expired
                retryWithNewToken(careerName, authRepository)
            } else {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun retryWithNewToken(careerName: String, authRepository: AuthRepository) {
        lifecycleScope.launch {
            val newToken = authRepository.refreshAccessToken()
            if (newToken != null) {
                // Simpan token baru dan ulangi request
                fetchCareerDetailsWithRetry(careerName, newToken, authRepository)
            } else {
                Toast.makeText(requireContext(), "Gagal memperbarui token, silakan login ulang", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
