package com.budi.caquapplicaton.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.budi.caquapplication.R
import com.budi.caquapplicaton.regist.LoginPage
import com.budi.caquapplicaton.ui.change.ChangePassword

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind views
        val ivProfilePicture = view.findViewById<ImageView>(R.id.ivProfilePicture)
        val ivEditProfile = view.findViewById<ImageView>(R.id.ivEditProfile)
        val tvProfileName = view.findViewById<TextView>(R.id.tvProfileName)
        val tvStats = view.findViewById<TextView>(R.id.tvStats)
        val statsSection = view.findViewById<LinearLayout>(R.id.statsSection)
        val tvChangePassword = view.findViewById<TextView>(R.id.tvChangePassword)
        val tvTerms = view.findViewById<TextView>(R.id.tvTerms)
        val btnLogOut = view.findViewById<Button>(R.id.btnLogOut)

        // Initialize the ViewModel with context
        profileViewModel.initialize(requireContext())

        // Observe LiveData from ViewModel
        profileViewModel.username.observe(viewLifecycleOwner, Observer { username ->
            tvProfileName.text = username
        })

        profileViewModel.userStats.observe(viewLifecycleOwner, Observer { stats ->
            tvStats.text = stats
        })

        profileViewModel.shouldNavigateToChangePassword.observe(viewLifecycleOwner, Observer { shouldNavigate ->
            if (shouldNavigate) {
                val intent = Intent(requireContext(), ChangePassword::class.java)
                startActivity(intent)
            }
        })

        profileViewModel.shouldNavigateToTermsCondition.observe(viewLifecycleOwner, Observer { shouldNavigate ->
            if (shouldNavigate) {
                val intent = Intent(requireContext(), TermsCondition::class.java)
                startActivity(intent)
            }
        })

        profileViewModel.shouldLogout.observe(viewLifecycleOwner, Observer { shouldLogout ->
            if (shouldLogout) {
                val intent = Intent(requireContext(), LoginPage::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
        })

        // Set click listeners
        tvChangePassword.setOnClickListener {
            profileViewModel.onChangePasswordClicked()
        }

        tvTerms.setOnClickListener {
            profileViewModel.onTermsClicked()
        }

        btnLogOut.setOnClickListener {
            profileViewModel.onLogOutClicked()
        }
    }
}
