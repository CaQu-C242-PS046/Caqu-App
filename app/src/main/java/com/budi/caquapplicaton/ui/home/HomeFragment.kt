package com.budi.caquapplicaton.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.budi.caquapplication.R
import com.budi.caquapplication.databinding.FragmentHomeBinding
import com.budi.caquapplication.utils.SharedPreferencesHelper
import com.budi.caquapplicaton.ui.quiz.QuizActivity
import com.google.android.material.appbar.AppBarLayout

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout and initialize binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Hide action bar
        (activity as AppCompatActivity).supportActionBar?.hide()

        // Initialize SharedPreferencesHelper
        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())

        // Set welcome text with username
        val username = sharedPreferencesHelper.getUsername() ?: "User"
        binding.welcomeText.text = getString(R.string.halo, username)

        // Observe ViewModel (optional)
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.text.observe(viewLifecycleOwner) { welcomeText ->
            binding.welcomeText.text = welcomeText
        }

        // Set AppBarLayout collapse logic
        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            if (totalScrollRange + verticalOffset == 0) {
                // Collapsed
                binding.toolbarMessage.visibility = View.GONE
            } else {
                // Expanded
                binding.toolbarMessage.visibility = View.VISIBLE
            }
        })

        // Set click listener for recommendation button
        binding.recommendButton.setOnClickListener {
            // Example action: Navigate to another screen
            startActivity(Intent(requireContext(), QuizActivity::class.java))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
