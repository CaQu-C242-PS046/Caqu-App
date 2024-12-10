package com.budi.caquapplicaton.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.budi.caquapplication.R
import com.budi.caquapplication.databinding.FragmentHomeBinding
import com.budi.caquapplicaton.room.AppDatabase
import com.budi.caquapplicaton.ui.quiz.QuizActivity
import com.budi.caquapplicaton.utils.SharedPreferencesHelper

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var database: AppDatabase
    private lateinit var adapter: RecommendationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.hide()

        // Initialize SharedPreferencesHelper
        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())

        // Initialize database
        database = AppDatabase.getInstance(requireContext())

        // Setup RecyclerView
        adapter = RecommendationAdapter()
        binding.historyRecycle.adapter = adapter

        // Observe data from database
        database.careerRecommendationDao().getAllRecommendations().observe(viewLifecycleOwner) { recommendations ->
            adapter.setData(recommendations)
        }

        // Welcome text
        val username = sharedPreferencesHelper.getUsername() ?: "User"
        binding.welcomeText.text = getString(R.string.halo, username)

        binding.recommendButton.setOnClickListener {
            startActivity(Intent(requireContext(), QuizActivity::class.java))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

