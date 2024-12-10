package com.budi.caquapplicaton.ui.career

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.budi.caquapplication.databinding.FragmentCareerBinding
import com.budi.caquapplicaton.retrofit.BaseService
import com.budi.caquapplicaton.retrofit.RetrofitClient
import com.budi.caquapplicaton.utils.SharedPreferencesHelper


class CareerFragment : Fragment() {

    private var _binding: FragmentCareerBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private val apiService = RetrofitClient.createService(BaseService::class.java)
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

        // Inisialisasi SharedPreferencesHelper
        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())

        // Ambil token dari SharedPreferences
        val token = sharedPreferencesHelper.getAccessToken()

        if (token.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Token tidak ditemukan, silakan login ulang", Toast.LENGTH_SHORT).show()
            return
        }

        // Fetch data karir
        val careerName = "Ekonom"
        viewModel.fetchCareerDetails(careerName, "Bearer $token")

        // Observe LiveData
        viewModel.careerDetails.observe(viewLifecycleOwner) { careerResponse ->
            binding.careerName.text = careerResponse.namaKarir
            binding.descriptionText.text = careerResponse.insight.joinToString("\n")
            binding.skillsText.text = careerResponse.skill.joinToString("\n")
            binding.educationText.text = careerResponse.pendidikan.joinToString("\n")
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

