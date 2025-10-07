package com.example.gameguesser.ui.settings

import android.content.Context
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

        //Navigate to Delete Account Profile
        binding.btnDeleteAccount.setOnClickListener {
            findNavController().navigate(R.id.deleteAccountFragment)
        }

        //Navigate to Help
        binding.btnHelp.setOnClickListener {
            findNavController().navigate(R.id.helpFragment)
        }

        //Navigate to Terms and conditions
        binding.btnTandC.setOnClickListener {
            findNavController().navigate(R.id.termsFragment)
        }

        //Navigate to Languages
        binding.btnLanguages

            .setOnClickListener {
                findNavController().navigate(R.id.languageFragment)
            }

        binding.btnLogout.setOnClickListener {
            // signs them out of ggole and the app
            val googleSignInClient = GoogleSignIn.getClient(
                requireContext(),
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
                    .build()
            )
            googleSignInClient.signOut().addOnCompleteListener {
                // clears shared pref
                val prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                prefs.edit().clear().apply()

                // goes back to login activity
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
