package com.budi.caquapplicaton.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.budi.caquapplication.databinding.FragmentHomeBinding
import com.budi.caquapplicaton.ui.quiz.QuizActivity
import com.budi.caquapplicaton.utils.SharedPreferencesHelper

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.hide()


        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())


        val username = sharedPreferencesHelper.getUsername() ?: "User"


        binding.welcomeText.text = "Welcome, $username!"


        homeViewModel.text.observe(viewLifecycleOwner) { welcomeText ->
            binding.welcomeText.text = welcomeText
        }


        binding.subtitleText.text = "“The best time to start was yesterday. The next best time is now.”"


        binding.recommendButton.setOnClickListener {
            val intent = Intent(requireContext(), QuizActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
