package com.budi.caquapplicaton.ui.softskill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.budi.caquapplication.databinding.FragmentSoftskillBinding
import com.budi.caquapplication.utils.SharedPreferencesHelper
import kotlinx.coroutines.launch

class SoftskillFragment : Fragment() {

    private var _binding: FragmentSoftskillBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SoftSkillAdapter
    private lateinit var viewModel: SoftskillViewModel
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSoftskillBinding.inflate(inflater, container, false)
        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())

        viewModel = ViewModelProvider(
            this,
            SoftskillViewModelFactory(sharedPreferencesHelper)
        )[SoftskillViewModel::class.java]

        setupRecyclerView()
        observeViewModel()

        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.fetchSoftSkills()
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvSoftSkills.layoutManager = LinearLayoutManager(requireContext())
        adapter = SoftSkillAdapter(emptyList())
        binding.rvSoftSkills.adapter = adapter

        adapter.setOnItemClickListener { name ->
            lifecycleScope.launch {
                viewModel.fetchSoftSkillDetail(name)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.softSkills.observe(viewLifecycleOwner) { softSkills ->
            adapter.updateData(softSkills)
            binding.progressBar.visibility = View.GONE
        }


        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
