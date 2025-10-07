package com.example.gameguesser.ui.settings

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gameguesser.databinding.FragmentEditProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get current signed-in Google account
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (account == null) {
            Toast.makeText(requireContext(), "No Google account found", Toast.LENGTH_SHORT).show()
            return
        }

        // Show Google account info
        binding.tvEmail.text = account.email ?: "N/A"
        binding.tvEmail.isEnabled = false

        // Load saved username and phone (stored locally)
        val prefs = requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        binding.etUsername.setText(prefs.getString("username", account.displayName ?: ""))
        binding.etPhone.setText(prefs.getString("phone", ""))

        // Handle save button click
        binding.btnSaveChanges.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()

            if (username.isEmpty() || phone.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Confirm Changes")
                .setMessage("Are you sure you want to save these changes?")
                .setPositiveButton("Yes") { _, _ ->
                    saveProfileChanges(username, phone)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun saveProfileChanges(username: String, phone: String) {
        // Save updates locally using SharedPreferences
        val prefs = requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        prefs.edit()
            .putString("username", username)
            .putString("phone", phone)
            .apply()

        Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
