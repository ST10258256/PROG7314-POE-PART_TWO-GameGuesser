package com.example.gameguesser.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.gameguesser.LoginActivity
import com.example.gameguesser.R
import com.example.gameguesser.databinding.FragmentSettingsBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val vm: SettingsViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        // Get the current Google user
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (account != null) {
            binding.tvDisplayName.text = account.displayName ?: "Guest"
            // Load profile picture using Glide
            account.photoUrl?.let {
                Glide.with(this)
                    .load(it)
                    .placeholder(R.drawable.ic_person) // fallback icon
                    .into(binding.profileImage)
            }
        } else {
            binding.tvDisplayName.text = "Guest"
        }

        //Navigate to Edit Profile
        binding.btnProfile.setOnClickListener {
            findNavController().navigate(R.id.editProfileFragment)
        }

        // Logout Functionality
        binding.btnLogout.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener {
                Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

                // Launch the LoginActivity and clear the back stack
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
