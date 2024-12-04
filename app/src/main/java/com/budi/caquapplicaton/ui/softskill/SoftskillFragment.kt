package com.budi.caquapplicaton.ui.softskill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.budi.caquapplication.databinding.FragmentSoftskillBinding
import kotlinx.coroutines.launch

class SoftskillFragment : Fragment() {

    private var _binding: FragmentSoftskillBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SoftSkillAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val softskillViewModel =
            ViewModelProvider(this).get(SoftskillViewModel::class.java)

        _binding = FragmentSoftskillBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Setup RecyclerView
        binding.rvEvent.layoutManager = LinearLayoutManager(requireContext())
        adapter = SoftSkillAdapter(emptyList())
        binding.rvEvent.adapter = adapter

        // Observe LiveData
        softskillViewModel.softSkills.observe(viewLifecycleOwner) { softSkills ->
            adapter = SoftSkillAdapter(softSkills)
            binding.rvEvent.adapter = adapter
            binding.rvEvent.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }

        softskillViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE
        }

        // Fetch Data
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            softskillViewModel.fetchSoftSkills()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
